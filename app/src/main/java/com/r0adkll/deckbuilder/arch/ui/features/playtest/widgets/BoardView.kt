package com.r0adkll.deckbuilder.arch.ui.features.playtest.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Vibrator
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.*
import android.view.animation.DecelerateInterpolator
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.ftinc.kit.kotlin.extensions.spToPx
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.playtest.Board
import com.r0adkll.deckbuilder.arch.domain.features.playtest.Board.Player
import com.r0adkll.deckbuilder.arch.domain.features.playtest.Board.Player.Type.*
import com.r0adkll.deckbuilder.arch.ui.features.playtest.widgets.BoardView.BoardElement.*
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.util.extensions.*
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.Type
import timber.log.Timber
import java.lang.IllegalArgumentException
import java.lang.annotation.ElementType
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * This is the view that renders and manages the board for a Playtest simulator session renderering
 * the [com.r0adkll.deckbuilder.arch.domain.features.playtest.Board] state
 */
class BoardView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr), GestureDetector.OnGestureListener {

    /**
     * Custom layout parameters that define how the children of this view are layed out
     * and searchable.
     */
    class LayoutParams(
            var playerType: Player.Type? = null,
            var element: BoardElement,
            var benchPosition: Int = 0
    ) : ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT) {

        companion object {

            /**
             * Special value for the height or width requested by a View.
             * WRAP_CONTENT means that the view wants to be just large enough to fit
             * its own internal content, taking its own padding into account.
             */
            const val WRAP_CONTENT = -2
        }
    }

    /**
     * An interface to define draggable behavior by the children of this view
     */
    interface Draggable {

        /**
         * The type of card that we are dragging
         */
        val cardType: SuperType

        /**
         * The subtype of the card that we are dragging
         */
        val cardSubtype: SubType

        /**
         * Notify implementation that this element has started to be dragged
         */
        fun onDragStart()

        /**
         * Notify implementation that it's drag has stopped
         */
        fun onDragStopped()
    }

    /**
     * The board action listener that defines callback behavior for user based events in this
     * view
     */
    interface BoardListener {

        /**
         * Called on a card view clicked on the board by the user
         * @param view the [BoardCardView] that was clicked. Information can be gleaned of it's layout-params
         */
        fun onCardClicked(view: BoardCardView)

        /**
         * Called when one of the [CardStackView]s are clicked on the board. This includes the
         * deck, discard, lost zone, or prizes
         * @param view the [CardStackView] that was clicked
         */
        fun onCardStackClicked(view: CardStackView)

        /**
         * Called when a user dragged and dropped a card view onto one of the valid drop targets on the
         * board.
         * @param view the card view that was dragged and dropped
         * @param targetType the element type of the target that the card was dropped on
         * @param targetElement the target element itself that the card was dropped on. important for [BENCH] types in determining which card on the bench it was dropped on.
         */
        fun onCardDropped(view: BoardCardView, targetType: BoardElement, targetElement: Element): Boolean

        /**
         * Called when an empty board element was clicked.
         * @param playerType the [Player.Type] side of the board that was clicked
         * @param elementType the type of board element that was clicked
         * @param element the board element itself that was clicked
         */
        fun onBoardElementClicked(playerType: Player.Type, elementType: BoardElement, element: Element)

        /**
         * Called when an empty board element was long clicked.
         * @param playerType the [Player.Type] side of the board that was clicked
         * @param elementType the type of board element that was clicked
         * @param element the board element itself that was clicked
         */
        fun onBoardElementLongClicked(playerType: Player.Type, elementType: BoardElement, element: Element)
    }

    /**
     * Per-Player board elements to be represented as [Element]s
     */
    enum class BoardElement {
        BENCH,
        DECK,
        DISCARD,
        ACTIVE,
        PRIZES,
        LOST_ZONE,
        STADIUM
    }

    /**
     * Represents the elements of the board/playmat for use in associated location and behavior
     * for the cards on the board and user behavior
     */
    sealed class Element {

        abstract val bounds: RectF

        class Card(override var bounds: RectF = RectF()) : Element()

        class Bench(val cards: MutableList<Card> = ArrayList() /* 5 - 8 Single Elements */) : Element() {

            override val bounds: RectF
                get() {
                    val left = cards.minBy { it.bounds.left }?.bounds?.left ?: 0f
                    val right = cards.maxBy { it.bounds.right }?.bounds?.right ?: 0f
                    val top = cards.minBy { it.bounds.top }?.bounds?.top ?: 0f
                    val bottom = cards.maxBy { it.bounds.bottom }?.bounds?.bottom ?: 0f
                    return RectF(left, top, right, bottom)
                }
        }
    }

    private data class Result(
            val type: Player.Type,
            val elementType: BoardElement,
            val element: Element
    )

    /**
     * Object that represents a current touch & drag session intiated by the user to track where
     * the drag began, the child it's dragging, and other useful state information
     */
    private class TouchSession(val downX: Float, val downY: Float) {

        var draggedChild: WeakReference<View>? = null
        var lastHoverResult: Result? = null
    }


    var board: Board? = null
        set(value) {
            field = value
            updateBoardView()
        }

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val elementPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val elementTextPaint: Paint = Paint(Paint.LINEAR_TEXT_FLAG)
    private val cardPunchPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val boardPadding: Float = dpToPx(24f)
    private val elementMargin: Float = dpToPx(16f)
    private val cardRadius = dpToPx(4f)
    private val cardPunchPadding = dpToPx(2f)

    var elementWidth: Float = 0f; private set
    var elementHeight: Float = 0f; private set

    private val vibrator by systemService<Vibrator>(context, Context.VIBRATOR_SERVICE)
    private val gestureDetector: GestureDetector
    private var listener: BoardListener? = null
    private val cardViewClickListener = OnClickListener {
        when(it) {
            is BoardCardView -> listener?.onCardClicked(it)
            is CardStackView -> listener?.onCardStackClicked(it)
        }
    }

    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private var touchSession: TouchSession? = null

    /**
     * A Map of Maps to contain all the positional element components of a playmat board
     * @see BoardElement
     * @see Element
     */
    private val boardElements: MutableMap<Player.Type, Map<BoardElement, Element>> = EnumMap(Player.Type::class.java)


    init {
        clipChildren = false
        clipToOutline = false
        setWillNotDraw(false)

        paint.color = color(R.color.playmat)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = dpToPx(8f)
        paint.strokeCap = Paint.Cap.ROUND

        elementPaint.color = color(R.color.playmat)
        elementPaint.style = Paint.Style.STROKE
        elementPaint.strokeWidth = dpToPx(4f)
        elementPaint.strokeCap = Paint.Cap.ROUND

        elementTextPaint.color = color(R.color.playmat)
        elementTextPaint.textSize = spToPx(14f)
        elementTextPaint.typeface = Typeface.DEFAULT_BOLD
        elementTextPaint.textAlign = Paint.Align.CENTER

        cardPunchPaint.color = Color.WHITE
        cardPunchPaint.style = Paint.Style.FILL_AND_STROKE
        cardPunchPaint.strokeWidth = dpToPx(4f)

        // Pre-Allocate all board elements
        val stadium = Element.Card()
        val player = HashMap<BoardElement, Element>()
        player[BENCH] = Element.Bench((0 until 5).map { Element.Card() }.toMutableList())
        player[DECK] = Element.Card()
        player[DISCARD] = Element.Card()
        player[PRIZES] = Element.Card()
        player[ACTIVE] = Element.Card()
        player[LOST_ZONE] = Element.Card()
        player[STADIUM] = stadium

        val opponent = HashMap<BoardElement, Element>()
        opponent[BENCH] = Element.Bench((0 until 5).map { Element.Card() }.toMutableList())
        opponent[DECK] = Element.Card()
        opponent[DISCARD] = Element.Card()
        opponent[PRIZES] = Element.Card()
        opponent[ACTIVE] = Element.Card()
        opponent[LOST_ZONE] = Element.Card()
        opponent[STADIUM] = stadium

        boardElements[PLAYER] = player
        boardElements[OPPONENT] = opponent

        gestureDetector = GestureDetector(context, this)

        /*
         * FIXME: TESTING
         */
        val poke = pokemon {
            id = "sm8-205"
            name = "Alolan Ninetales-GX"
            nationalPokedexNumber = 38
            hp = 200
            imageUrl = "https://images.pokemontcg.io/sm8/205.png"
            imageUrlHiRes = "https://images.pokemontcg.io/sm8/205_hires.png"
        }
        val poke2 = pokemon {
            id = "sm8-53"
            name = "Alolan Vulpix"
            nationalPokedexNumber = 37
            hp = 260
            imageUrl = "https://images.pokemontcg.io/sm8/53.png"
            imageUrlHiRes = "https://images.pokemontcg.io/sm8/53_hires.png"
        }

        val energy = pokemon {
            id = "sm1-171"
            name = "Fairy Energy"
            supertype = SuperType.ENERGY
            subtype = SubType.BASIC
            types = listOf(Type.FAIRY)
        }

        val tool = pokemon {
            id = "sm8-190"
            name = "Spell Tag"
            imageUrl = "https://images.pokemontcg.io/sm8/190.png"
            imageUrlHiRes = "https://images.pokemontcg.io/sm8/190_hires.png"
            supertype = SuperType.TRAINER
            subtype = SubType.POKEMON_TOOL
        }

        val pokeCard = Board.Card(
                arrayDequeOf(poke),
                listOf(energy, energy.copy(), energy.copy()),
                listOf(tool),
                isPoisoned = false,
                isBurned = false,
                statusEffect = null,
                damage = 50
        )

        val poke2Card = Board.Card(
                arrayDequeOf(poke2),
                listOf(energy.copy())
        )

        board = Board(
                Player(
                        listOf(poke, poke.copy(), poke.copy(), poke.copy(), poke.copy(), poke.copy(), poke.copy()),
                        mapOf(
                                0 to poke.copy(),
                                1 to energy.copy(),
                                2 to energy.copy(),
                                3 to tool.copy(),
                                4 to poke.copy(),
                                5 to poke.copy()
                        ),
                        ArrayDeque((0..45).map { poke.copy() }),
                        listOf(energy.copy(), tool.copy()),
                        emptyList(),
                        Board.Bench(mapOf(
                                0 to pokeCard.copy(),
                                2 to poke2Card.copy(),
                                3 to poke2Card.copy()
                        )),
                        pokeCard.copy(statusEffect = Board.Card.Status.SLEEPING),
                        null
                ),
                Player(emptyList(), emptyMap(), ArrayDeque(0), emptyList(), emptyList(), Board.Bench(), null, null),
                Board.Turn(0, PLAYER)
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        elementWidth = (measuredWidth.toFloat() -  ((boardPadding * 2)  +  elementMargin * 4)) / 5f
        elementHeight = elementWidth * PokemonCardView.RATIO

        forEach {
            measureChild(it, widthMeasureSpec, heightMeasureSpec)
        }

        // Calculate element positioning for both Player.Type's

        // Player.Type.PLAYER
        run {

            /*
             * Player Bench
             */
            val baseX = boardPadding
            val baseY = measuredHeight - (boardPadding + elementHeight)
            val benchElement = boardElements[PLAYER]!![BENCH] as Element.Bench
            (0 until 5).forEach {
                val card = benchElement.cards[it]
                val offsetX = (it * elementWidth) + (it * elementMargin)
                card.bounds.set(baseX + offsetX, baseY, baseX + offsetX + elementWidth, baseY + elementHeight)
            }

            /*
             * Player Discard & Deck
             */
            val deckElement = boardElements[PLAYER]!![DECK] as Element.Card
            val discardElement = boardElements[PLAYER]!![DISCARD] as Element.Card

            val deckX = baseX + (4 * elementWidth) + (4 * elementMargin)
            val deckY = baseY - ((elementHeight + elementMargin) * 2) + (elementMargin / 2)

            deckElement.bounds.set(deckX, deckY, deckX + elementWidth, deckY + elementHeight)
            val discardY = deckY + (elementHeight + elementMargin / 2)
            discardElement.bounds.set(deckX, discardY, deckX + elementWidth, discardY + elementHeight)

            /*
             * Player Active
             */
            val activeElement = boardElements[PLAYER]!![ACTIVE] as Element.Card
            val centerX = measuredWidth / 2f
            val centerY = measuredHeight / 2f
            val radius = measuredWidth / 5f
            val innerRadius = radius * .40f
            val activeX = centerX - (elementWidth / 2f)
            val activeY = centerY + (innerRadius  / 2f)
            activeElement.bounds.set(activeX, activeY, activeX + elementWidth, activeY + elementHeight)

            /*
             * Player Prizes
             */
            val prizesElement = boardElements[PLAYER]!![PRIZES] as Element.Card
            val prizesX = baseX
            val prizesY = discardY
            prizesElement.bounds.set(prizesX, prizesY, prizesX + elementWidth, prizesY + elementHeight)

            /*
             * Player Lost Zone
             */
            val lostZoneElement = boardElements[PLAYER]!![LOST_ZONE] as Element.Card
            val lostZoneX = deckX - (elementWidth + elementMargin)
            val lostZoneY = discardY
            lostZoneElement.bounds.set(lostZoneX, lostZoneY, lostZoneX + elementWidth, lostZoneY + elementHeight)

            /*
             * Agnostic Stadium Position
             */
            val stadiumElement = boardElements[PLAYER]!![STADIUM] as Element.Card
            val stadiumX = activeX - (elementWidth + elementMargin)
            val stadiumY = centerY - (elementHeight / 2f)
            stadiumElement.bounds.set(stadiumX, stadiumY, stadiumX + elementWidth, stadiumY + elementHeight)
        }

        // Player.Type.OPPONENT
        run {
            /*
             * Opponent Bench
             */
            val benchElement = boardElements[OPPONENT]!![BENCH] as Element.Bench
            (1 until 6).forEach {
                val card = benchElement.cards[it - 1]
                val offsetX = measuredWidth - ((it * elementWidth) + ((it - 1) * elementMargin))
                val x = offsetX - boardPadding
                val y = boardPadding
                card.bounds.set(x, y, x + elementWidth, y + elementHeight)
            }

            /*
             * Opponent Discard & Deck
             */
            val deckElement = boardElements[OPPONENT]!![DECK] as Element.Card
            val discardElement = boardElements[OPPONENT]!![DISCARD] as Element.Card

            val deckX = boardPadding
            val deckY = boardPadding + elementHeight + elementMargin + elementHeight + (elementMargin / 2f)

            deckElement.bounds.set(deckX, deckY, deckX + elementWidth, deckY + elementHeight)
            val discardY = deckY - (elementHeight + elementMargin / 2f)
            discardElement.bounds.set(deckX, discardY, deckX + elementWidth, discardY + elementHeight)

            /*
             * Opponent Active
             */
            val activeElement = boardElements[OPPONENT]!![ACTIVE] as Element.Card
            val centerX = measuredWidth / 2f
            val centerY = measuredHeight / 2f
            val radius = measuredWidth / 5f
            val innerRadius = radius * .40f
            val activeX = centerX - (elementWidth / 2f)
            val activeY = centerY - (innerRadius / 2f) - elementHeight
            activeElement.bounds.set(activeX, activeY, activeX + elementWidth, activeY + elementHeight)

            /*
             * Opponent Prizes
             */
            val prizesElement = boardElements[OPPONENT]!![PRIZES] as Element.Card
            val baseY = boardPadding + elementHeight + elementMargin
            val prizesX = measuredWidth - (boardPadding + elementWidth)
            val prizesY = discardY
            prizesElement.bounds.set(prizesX, prizesY, prizesX + elementWidth, prizesY + elementHeight)

            /*
             * Opponent Lost Zone
             */
            val lostZoneElement = boardElements[OPPONENT]!![LOST_ZONE] as Element.Card
            val lostZoneX = deckX + elementWidth + elementMargin
            val lostZoneY = discardY
            lostZoneElement.bounds.set(lostZoneX, lostZoneY, lostZoneX + elementWidth, lostZoneY + elementHeight)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val leftPos = paddingLeft
        val rightPos = r - l - paddingRight
        val topPos = paddingTop
        val bottomPos = b - t - paddingBottom

        forEach { child ->
            val lp = child.layoutParams as LayoutParams
            val element = boardElements[lp.playerType]?.get(lp.element)
            if (element != null) {
                when(element) {
                    is Element.Card -> child.layout(
                            element.bounds.left.toInt(),
                            element.bounds.top.toInt() - child.paddingTop
                    )
                    is Element.Bench -> {
                        val bounds = element.cards[lp.benchPosition].bounds
                        child.layout(
                                bounds.left.toInt(),
                                bounds.top.toInt() - child.paddingTop
                        )
                    }
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        //Draw the Active Arena
        drawPokeBall(canvas)

        // Draw Elements
        BoardElement.values().forEach { element ->
            if (element == BENCH) {
                val player = boardElements[PLAYER]!![element] as Element.Bench
                val opponent = boardElements[OPPONENT]!![element] as Element.Bench

                player.cards.forEach { drawCard(canvas, it.bounds, null, false) }
                opponent.cards.forEach { drawCard(canvas, it.bounds, null, false) }
            } else {
                drawCard(canvas, element)
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.x
        val y = ev.y

        when(ev.action) {
            MotionEvent.ACTION_DOWN -> {
                Timber.i("Touch session started @ (x=$x, y=$y)")
                touchSession = TouchSession(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                if (touchSession != null && touchSession?.draggedChild?.get() == null) {
                    val distance = sqrt((x - touchSession!!.downX).pow(2) + (y - touchSession!!.downY).pow(2))
                    if (distance > touchSlop) {
                        val draggedChild = findChild(touchSession!!.downX, touchSession!!.downY)
                        if (draggedChild != null && draggedChild is Draggable) {
                            Timber.i("Threshold for dragging has been reached and child found, start drag mode")
                            touchSession?.draggedChild = WeakReference(draggedChild)

                            draggedChild.translationX = x - touchSession!!.downX
                            draggedChild.translationY = y - touchSession!!.downY

                            (draggedChild as? Draggable)?.onDragStart()
                            return true
                        } else if (draggedChild != null && draggedChild !is Draggable) {
                            Timber.i("Touched child can NEVER be dragged, terminate the touch session")
                            touchSession = null
                        }
                    }
                }
            }
        }

        // Check if any of the children can intercept
        return findChild(x, y) == null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (touchSession == null || touchSession?.draggedChild?.get() == null) {
            return gestureDetector.onTouchEvent(event)
        } else {
            val x = event.x
            val y = event.y

            when(event.action) {
                MotionEvent.ACTION_MOVE -> {
                    touchSession?.draggedChild?.get()?.let { child ->
                        child.translationX = x - touchSession!!.downX
                        child.translationY = y - touchSession!!.downY

                        // Check if we have moved over a drop target
                        val cardType = (child as Draggable).cardType
                        val cardSubtype = (child as Draggable).cardSubtype
                        val childParams = child.layoutParams as LayoutParams
                        touchSession?.lastHoverResult = findBoardElements(x, y)?.also { result ->
                            if (result.type == childParams.playerType && result != touchSession?.lastHoverResult) {
                                when (result.elementType) {
                                    BENCH -> {
                                        // We don't want to vibrate when its the bench collection element
                                        if (cardType == SuperType.POKEMON && result.element !is Element.Bench) {
                                            performHoverHapticFeedback(child)
                                        }
                                    }
                                    DECK -> {
                                        performHoverHapticFeedback(child)
                                    }
                                    DISCARD -> {
                                        performHoverHapticFeedback(child)
                                    }
                                    ACTIVE -> {
                                        if (childParams.element == BENCH) {
                                            performHoverHapticFeedback(child)
                                        }
                                    }
                                    //PRIZES -> // Do Nothing()
                                    LOST_ZONE -> {
                                        performHoverHapticFeedback(child)
                                    }
                                    STADIUM -> {
                                        if (cardType == SuperType.TRAINER
                                                && cardSubtype == SubType.STADIUM) {
                                            performHoverHapticFeedback(child)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    (touchSession?.draggedChild?.get() as? Draggable)?.onDragStopped()
                    touchSession?.draggedChild?.get()?.let { child ->
                        val result = findBoardElements(event.x, event.y)
                        if (result != null) {
                            val cardType = (child as Draggable).cardType
                            val cardSubtype = (child as Draggable).cardSubtype
                            val childParams = child.layoutParams as LayoutParams
                            if (result.type == childParams.playerType) {
                                when (result.elementType) {
                                    BENCH -> {
                                        // We don't want\" to vibrate when its the bench collection element
                                        if (cardType == SuperType.POKEMON && result.element !is Element.Bench) {
                                            performHoverHapticFeedback(child, 16)
                                            if (listener?.onCardDropped(child as BoardCardView, result.elementType, result.element) != true) {
                                                child.resetChildCardView()
                                            }
                                        }
                                    }
                                    DECK -> {
                                        performHoverHapticFeedback(child, 16)
                                        if (listener?.onCardDropped(child as BoardCardView, result.elementType, result.element) != true) {
                                            child.resetChildCardView()
                                        }
                                    }
                                    DISCARD -> {
                                        performHoverHapticFeedback(child, 16)
                                        if (listener?.onCardDropped(child as BoardCardView, result.elementType, result.element) != true) {
                                            child.resetChildCardView()
                                        }
                                    }
                                    ACTIVE -> {
                                        if (childParams.element == BENCH) {
                                            performHoverHapticFeedback(child, 16)
                                            if (listener?.onCardDropped(child as BoardCardView, result.elementType, result.element) != true) {
                                                child.resetChildCardView()
                                            }
                                        }
                                    }
                                    //PRIZES -> // Do Nothing()
                                    LOST_ZONE -> {
                                        performHoverHapticFeedback(child, 16)
                                        if (listener?.onCardDropped(child as BoardCardView, result.elementType, result.element) != true) {
                                            child.resetChildCardView()
                                        }
                                    }
                                    STADIUM -> {
                                        if (cardType == SuperType.TRAINER
                                                && cardSubtype == SubType.STADIUM) {
                                            performHoverHapticFeedback(child, 16)
                                            if (listener?.onCardDropped(child as BoardCardView, result.elementType, result.element) != true) {
                                                child.resetChildCardView()
                                            }
                                        }
                                    }
                                    else -> child.resetChildCardView()
                                }
                            } else {
                                child.resetChildCardView()
                            }
                        } else {
                            child.resetChildCardView()
                        }
                    }
                    touchSession = null
                }
                MotionEvent.ACTION_CANCEL -> {
                    (touchSession?.draggedChild?.get() as? Draggable)?.onDragStopped()
                    touchSession?.draggedChild?.get()?.let { child ->
                        child.resetChildCardView()
                    }
                    touchSession = null
                }
            }
            return true
        }
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        if (params !is LayoutParams) throw IllegalArgumentException("params must be BoardView.LayoutParams")
        super.addView(child, params)
    }

    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        val x = e.x
        val y = e.y

        // Find a board element that this might contain the xy
        findBoardElements(x, y)?.let { result ->
            performClick()
            listener?.onBoardElementClicked(result.type, result.elementType, result.element)
            return true
        }

        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return false
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent) {
        val x = e.x
        val y = e.y

        // Find a board element that this might contain the xy
        findBoardElements(x, y)?.let { result ->
            performLongClick()
            listener?.onBoardElementLongClicked(result.type, result.elementType, result.element)
        }
    }

    fun setBoardListener(listener: BoardListener?) {
        this.listener = listener
    }

    private fun updateBoardView() {
        board?.let { b ->

            /*
             * Find and update the stadium
             */
            b.stadium?.let { s ->
                var stadiumViewChild = findChild<BoardCardView>(null, STADIUM)
                if (stadiumViewChild == null) {
                    stadiumViewChild = BoardCardView(context)
                    val lp = LayoutParams(null, STADIUM)
                    addView(stadiumViewChild, lp)
                }

                stadiumViewChild.card = Board.Card(arrayDequeOf(s))
            } ?: removeChild(null, STADIUM)

            /**
             * Find and update the player and opponent board positions
             */
            updatePlayer(PLAYER, b.player)
            updatePlayer(OPPONENT, b.opponent)
        }
    }

    private fun updatePlayer(type: Player.Type, player: Player) {
        /*
         * Find & Update the Player, Active card if exists
         */
        player.active?.let { active ->
            var activeViewChild = findChild<BoardCardView>(type, ACTIVE)
            if (activeViewChild == null) {
                activeViewChild = BoardCardView(context)
                activeViewChild.setOnClickListener(cardViewClickListener)
                val lp = LayoutParams(type, ACTIVE)
                addView(activeViewChild, lp)
            }

            activeViewChild.card = active
        } ?: removeChild(type, ACTIVE)

        /*
         * Find and Update the bench cards
         */
        player.bench.cards.forEach { (position, card) ->
            var benchView = findChild<BoardCardView>(type, BENCH, position)

            if (benchView == null) {
                benchView = BoardCardView(context)
                benchView.setOnClickListener(cardViewClickListener)
                val lp = LayoutParams(type, BENCH, position)
                addView(benchView, lp)
            }

            benchView.card = card
        }

        // Remove any bench children that aren't in the board state
        removeChildren(type, player.bench.cards.keys)

        /*
         * Find & Update a Player's deck
         */
        findAndUpdateCardStackView(type, DECK, player.deck, true)

        /*
         * Find & Update a Player's discard stack
         */
        findAndUpdateCardStackView(type, DISCARD, player.discard)

        /*
         * Find & Update a Player's lostZone stack
         */
        findAndUpdateCardStackView(type, LOST_ZONE, player.lostZone)

        /*
         * Find & Update a Player's Prize card stack
         */
        findAndUpdateCardStackView(type, PRIZES, player.prizes.values, true)
    }

    private fun findAndUpdateCardStackView(
            playerType: Player.Type,
            boardElement: BoardElement,
            elements: Collection<PokemonCard>,
            startFaceDown: Boolean = false
    ) {
        if (elements.isNotEmpty()) {
            var stackView = findChild<CardStackView>(playerType, boardElement)

            if (stackView == null) {
                stackView = CardStackView(
                        context,
                        cards = elements.map { Board.Card(arrayDequeOf(it)) },
                        startFaceDown = startFaceDown
                )
                stackView.setOnClickListener(cardViewClickListener)
                val lp = LayoutParams(playerType, boardElement)
                addView(stackView, lp)
            } else {
                stackView.cards = elements.map { Board.Card(arrayDequeOf(it)) }
            }
        } else {
            removeChild(playerType, boardElement)
        }
    }

    private fun findBoardElements(x: Float, y: Float): Result? {
        boardElements.forEach { entry ->
            val type = entry.key
            entry.value.forEach {
                val elementType = it.key
                val element = it.value

                if (element is Element.Bench) {
                    element.cards.forEach { benchElement ->
                        if (benchElement.bounds.contains(x, y)) {
                            return Result(type, elementType, benchElement)
                        }
                    }
                }

                if (element.bounds.contains(x, y)) {
                    return Result(type, elementType, it.value)
                }
            }
        }
        return null
    }


    private fun drawCard(canvas: Canvas, element: BoardElement) {
        val border = element == ACTIVE || element == STADIUM || element == LOST_ZONE
        val text = when(element) {
            DECK -> "DECK"
            DISCARD -> "DISC."
            LOST_ZONE -> "LOST\nZONE"
            PRIZES -> "PRIZES"
            else -> null
        }

        val player = boardElements[PLAYER]!![element]
        val opponent = boardElements[OPPONENT]!![element]
        player?.bounds?.let { drawCard(canvas, it, text, border) }
        opponent?.bounds?.let { drawCard(canvas, it, text, border) }
    }


    private fun drawCard(canvas: Canvas, bounds: RectF, text: String? = null, border: Boolean = false) {
        if (border) {
            canvas.drawRoundRect(bounds.left - cardPunchPadding, bounds.top - cardPunchPadding,
                    bounds.right + cardPunchPadding, bounds.bottom + cardPunchPadding,
                    cardRadius, cardRadius, cardPunchPaint)
        }
        canvas.drawRoundRect(bounds, cardRadius, cardRadius, elementPaint)
        if (text != null) {
            val p = TextPaint(elementTextPaint)
            val width = text.split("\n").maxBy { p.measureText(it) }?.let { p.measureText(it).toInt() } ?: p.measureText(text).toInt()
            val layout = StaticLayout(text, p, width, Layout.Alignment.ALIGN_CENTER, 1f, 0f, false)
            val height = layout.height
            val count = canvas.save()
            val dX = bounds.left + (elementWidth / 2f)
            val dY = (bounds.top + elementHeight / 2f) - height / 2f
            canvas.translate(dX, dY)
            layout.draw(canvas)
            canvas.restoreToCount(count)
        }
    }


    private fun drawPokeBall(canvas: Canvas) {
        val centerX = measuredWidth / 2f
        val centerY = measuredHeight / 2f
        val radius = measuredWidth / 5f
        val innerRadius = radius * .40f

        canvas.drawCircle(centerX, centerY, innerRadius, paint)
        canvas.drawCircle(centerX, centerY, radius, paint)
        canvas.drawLine(centerX - radius, centerY, centerX - innerRadius, centerY, paint)
        canvas.drawLine(centerX + innerRadius, centerY, centerX + radius, centerY, paint)
    }

    private fun removeChild(
            type: Player.Type?,
            element: BoardElement,
            benchPosition: Int = 0
    ) {
        val view = findChild<View>(type, element, benchPosition)
        if (view != null) {
            removeView(view)
        }
    }

    private fun removeChildren(
            type: Player.Type?,
            excludeBenchPositions: Iterable<Int>
    ) {
        val childrenToRemove = ArrayList<View>()

        forEach { child ->
            val lp = child.layoutParams as? LayoutParams
            if (lp != null
                    && lp.playerType == type
                    && lp.element == BENCH
                    && !excludeBenchPositions.contains(lp.benchPosition)) {
                childrenToRemove += child
            }
        }

        childrenToRemove.forEach {
            removeView(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : View> findChild(
            type: Player.Type?,
            element: BoardElement,
            benchPosition: Int = 0
    ): T? {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childLp = child.layoutParams
            if (childLp is LayoutParams
                    && childLp.playerType == type
                    && childLp.element == element
                    && childLp.benchPosition == benchPosition) {
                return child as T
            }
        }
        return null
    }

    private fun findChild(x: Float, y: Float): View? {
        val hitRect = Rect()
        for (index in 0 until childCount) {
            val child = getChildAt(index)
            child.getHitRect(hitRect)
            if (hitRect.contains(x.toInt(), y.toInt())) {
                return child
            }
        }
        return null
    }

    private fun performHoverHapticFeedback(view: View, feedbackConstant: Int? = null) {
        view.performHapticFeedback(feedbackConstant ?: 11)
    }

    private fun View.resetChildCardView() {
        this.animate()
                .translationX(0f)
                .translationY(0f)
                .setDuration(300L)
                .setInterpolator(DecelerateInterpolator(2f))
                .start()
    }
}
