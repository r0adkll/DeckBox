package com.r0adkll.deckbuilder.arch.ui.widgets

import android.content.Context
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.ftinc.kit.kotlin.extensions.spToPx
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.playtest.Board.Player

/**
 * TODO: NAME TO CHANGE
 */
class ArenaPlaymatView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val elementPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val elementTextPaint: Paint = Paint(Paint.LINEAR_TEXT_FLAG)
    private val cardPunchPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val boardPadding: Float = dpToPx(24f)
    private val elementMargin: Float = dpToPx(16f)
    private var elementWidth: Float = 0f
    private var elementHeight: Float = 0f
    private val cardRadius = dpToPx(4f)
    private val cardPunchPadding = dpToPx(2f)

    /**
     * A Map of Maps to contain all the positional element components of a playmat board
     * @see BoardElement
     * @see Element
     */
    private val board: MutableMap<Player.Type, Map<BoardElement, Element>> = HashMap()


    init {
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
        player[BoardElement.BENCH] = Element.Bench((0 until 5).map { Element.Card() }.toMutableList())
        player[BoardElement.DECK] = Element.Card()
        player[BoardElement.DISCARD] = Element.Card()
        player[BoardElement.PRIZES] = Element.Card()
        player[BoardElement.ACTIVE] = Element.Card()
        player[BoardElement.LOST_ZONE] = Element.Card()
        player[BoardElement.STADIUM] = stadium

        val opponent = HashMap<BoardElement, Element>()
        opponent[BoardElement.BENCH] = Element.Bench((0 until 5).map { Element.Card() }.toMutableList())
        opponent[BoardElement.DECK] = Element.Card()
        opponent[BoardElement.DISCARD] = Element.Card()
        opponent[BoardElement.PRIZES] = Element.Card()
        opponent[BoardElement.ACTIVE] = Element.Card()
        opponent[BoardElement.LOST_ZONE] = Element.Card()
        opponent[BoardElement.STADIUM] = stadium

        board[Player.Type.PLAYER] = player
        board[Player.Type.OPPONENT] = opponent
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        elementWidth = (measuredWidth.toFloat() -  ((boardPadding * 2)  +  elementMargin * 4)) / 5f
        elementHeight = elementWidth * PokemonCardView.RATIO

        // Calculate element positioning for both Player.Type's

        // Player.Type.PLAYER
        run {

            /*
             * Player Bench
             */
            val baseX = boardPadding
            val baseY = measuredHeight - (boardPadding + elementHeight)
            val benchElement = board[Player.Type.PLAYER]!![BoardElement.BENCH] as Element.Bench
            (0 until 5).forEach {
                val card = benchElement.cards[it]
                val offsetX = (it * elementWidth) + (it * elementMargin)
                card.bounds.set(baseX + offsetX, baseY, baseX + offsetX + elementWidth, baseY + elementHeight)
            }

            /*
             * Player Discard & Deck
             */
            val deckElement = board[Player.Type.PLAYER]!![BoardElement.DECK] as Element.Card
            val discardElement = board[Player.Type.PLAYER]!![BoardElement.DISCARD] as Element.Card

            val deckX = baseX + (4 * elementWidth) + (4 * elementMargin)
            val deckY = baseY - ((elementHeight + elementMargin) * 2) + (elementMargin / 2)

            deckElement.bounds.set(deckX, deckY, deckX + elementWidth, deckY + elementHeight)
            val discardY = deckY + (elementHeight + elementMargin / 2)
            discardElement.bounds.set(deckX, discardY, deckX + elementWidth, discardY + elementHeight)

            /*
             * Player Active
             */
            val activeElement = board[Player.Type.PLAYER]!![BoardElement.ACTIVE] as Element.Card
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
            val prizesElement = board[Player.Type.PLAYER]!![BoardElement.PRIZES] as Element.Card
            val prizesX = baseX
            val prizesY = discardY
            prizesElement.bounds.set(prizesX, prizesY, prizesX + elementWidth, prizesY + elementHeight)

            /*
             * Player Lost Zone
             */
            val lostZoneElement = board[Player.Type.PLAYER]!![BoardElement.LOST_ZONE] as Element.Card
            val lostZoneX = deckX - (elementWidth + elementMargin)
            val lostZoneY = discardY
            lostZoneElement.bounds.set(lostZoneX, lostZoneY, lostZoneX + elementWidth, lostZoneY + elementHeight)

            /*
             * Agnostic Stadium Position
             */
            val stadiumElement = board[Player.Type.PLAYER]!![BoardElement.STADIUM] as Element.Card
            val stadiumX = activeX - (elementWidth + elementMargin)
            val stadiumY = centerY - (elementHeight / 2f)
            stadiumElement.bounds.set(stadiumX, stadiumY, stadiumX + elementWidth, stadiumY + elementHeight)
        }

        // Player.Type.OPPONENT
        run {
            /*
             * Opponent Bench
             */
            val benchElement = board[Player.Type.OPPONENT]!![BoardElement.BENCH] as Element.Bench
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
            val deckElement = board[Player.Type.OPPONENT]!![BoardElement.DECK] as Element.Card
            val discardElement = board[Player.Type.OPPONENT]!![BoardElement.DISCARD] as Element.Card

            val deckX = boardPadding
            val deckY = boardPadding + elementHeight + elementMargin + elementHeight + (elementMargin / 2f)

            deckElement.bounds.set(deckX, deckY, deckX + elementWidth, deckY + elementHeight)
            val discardY = deckY - (elementHeight + elementMargin / 2f)
            discardElement.bounds.set(deckX, discardY, deckX + elementWidth, discardY + elementHeight)

            /*
             * Opponent Active
             */
            val activeElement = board[Player.Type.OPPONENT]!![BoardElement.ACTIVE] as Element.Card
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
            val prizesElement = board[Player.Type.OPPONENT]!![BoardElement.PRIZES] as Element.Card
            val baseY = boardPadding + elementHeight + elementMargin
            val prizesX = measuredWidth - (boardPadding + elementWidth)
            val prizesY = discardY
            prizesElement.bounds.set(prizesX, prizesY, prizesX + elementWidth, prizesY + elementHeight)

            /*
             * Opponent Lost Zone
             */
            val lostZoneElement = board[Player.Type.OPPONENT]!![BoardElement.LOST_ZONE] as Element.Card
            val lostZoneX = deckX + elementWidth + elementMargin
            val lostZoneY = discardY
            lostZoneElement.bounds.set(lostZoneX, lostZoneY, lostZoneX + elementWidth, lostZoneY + elementHeight)
        }
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onDraw(canvas: Canvas) {
        /*
         * Draw the Active Arena
         */
        drawPokeBall(canvas)

        // Draw Elements
        BoardElement.values().forEach { element ->
            if (element == BoardElement.BENCH) {
                val player = board[Player.Type.PLAYER]!![element] as Element.Bench
                val opponent = board[Player.Type.OPPONENT]!![element] as Element.Bench

                player.cards.forEach { drawCard(canvas, it.bounds, null, false) }
                opponent.cards.forEach { drawCard(canvas, it.bounds, null, false) }
            } else {
                drawCard(canvas, element)
            }
        }
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {


        return super.onTouchEvent(event)
    }


    private fun drawCard(canvas: Canvas, element: BoardElement) {
        val border = element == BoardElement.ACTIVE || element == BoardElement.STADIUM || element == BoardElement.LOST_ZONE
        val text = when(element) {
            BoardElement.DECK -> "DECK"
            BoardElement.DISCARD -> "DISC."
            BoardElement.LOST_ZONE -> "LOST\nZONE"
            BoardElement.PRIZES -> "PRIZES"
            else -> null
        }

        val player = board[Player.Type.PLAYER]!![element]
        val opponent = board[Player.Type.OPPONENT]!![element]
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
}