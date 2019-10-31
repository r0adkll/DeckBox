package com.r0adkll.deckbuilder.arch.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView.Evolution.*

class EvolutionChainView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val inflater = LayoutInflater.from(context)
    private val linkPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val linkBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var defaultCardWidth: Int = dipToPx(156f)
    private var cardWidth: Int = defaultCardWidth
    private val linkSpacing: Int = dipToPx(24f)
    private val stageSpacing: Int = dipToPx(16f)
    private val nodeSpacing: Int = dipToPx(4f)
    private val chainSpacing: Int = dipToPx(8f)
    private val linkRadius: Float = dpToPx(8f)

    private var pokemonCardClickListener: OnPokemonCardClickListener? = null
    private var pokemonCardEditListener: OnPokemonEditListener? = null

    var evolutionChain: EvolutionChain? = null
        set(value) {
            field = value
            configurePokemonCardViews()
        }

    var dragAndDropEnabled: Boolean = false

    init {
        orientation = HORIZONTAL
        setWillNotDraw(false)
        defaultCardWidth = (dipToPx(382f) - (2 * stageSpacing + 2 * linkSpacing)) / 3
        cardWidth = defaultCardWidth

        linkPaint.color = color(R.color.primaryColor)
        linkPaint.style = Paint.Style.FILL

        linkBarPaint.color = color(R.color.primaryColor)
        linkBarPaint.style = Paint.Style.STROKE
        linkBarPaint.strokeWidth = linkRadius
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (evolutionChain != null) {
            val unitWidth = measuredWidth / evolutionChain!!.size
            cardWidth = unitWidth - linkSpacing
            (0 until childCount).forEach {
                val child = getChildAt(it)
                val lp = child.layoutParams
                lp.width = cardWidth
                child.layoutParams = lp
            }
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        (0 until childCount).forEach { index ->
            val childLayout = getChildAt(index)
            val child = childLayout.findViewById<PokemonCardView>(R.id.card)
            if (child.evolution == END || child.evolution == MIDDLE) {
                val nextChild = getChildAt(index + 1)?.findViewById<PokemonCardView>(R.id.card)
                if (nextChild != null && (nextChild.evolution == START || nextChild.evolution == MIDDLE)) {
                    // Render Link
                    val y = childLayout.top + (childLayout.height / 2f)

                    // Start node
                    val x1 = childLayout.right.toFloat()
                    canvas.drawCircle(x1, y, linkRadius, linkPaint)

                    // End node
                    val x2 = (nextChild.parent as ViewGroup).left.toFloat()
                    canvas.drawCircle(x2, y, linkRadius, linkPaint)

                    // Render connecting bar
                    canvas.drawLine(x1, y, x2, y, linkBarPaint)
                }
            }
        }
    }

    fun setOnPokemonCardClickListener(listener: (PokemonCardView) -> Unit) {
        pokemonCardClickListener = object : OnPokemonCardClickListener {
            override fun onPokemonCardClicked(view: PokemonCardView, card: PokemonCard) {
                listener.invoke(view)
            }
        }
    }

    fun setOnPokemonCardEditListener(onAdd: (PokemonCard) -> Unit, onRemove: (PokemonCard) -> Unit) {
        pokemonCardEditListener = object : OnPokemonEditListener {
            override fun onPokemonCardAdded(card: PokemonCard) {
                onAdd.invoke(card)
            }

            override fun onPokemonCardRemoved(card: PokemonCard) {
                onRemove.invoke(card)
            }
        }
    }

    @SuppressLint("NewApi")
    private fun configurePokemonCardViews() {
        //removeAllViews() // ???
        evolutionChain?.let { chain ->

            var node = chain.first()
            var nodeIndex = 0
            var viewIndex = 0
            while(node != null) {
                node.cards.forEachIndexed { cardIndex, card ->
                    // Attempt to find existing view for index
                    var view = getChildAt(viewIndex)
                    if (view == null) {
                        view = inflater.inflate(R.layout.item_pokemon_card_editable, this, false)
                        addView(view)
                    }

                    val cardView = view.findViewById<PokemonCardView>(R.id.card)
                    val actionLayout = view.findViewById<LinearLayout>(R.id.action_layout)
                    val actionAdd = view.findViewById<ImageView>(R.id.action_add)
                    val actionRemove = view.findViewById<ImageView>(R.id.action_remove)

                    // Only re-apply the card if it is different
                    if (cardView.card != card.card) {
                        cardView.card = card.card
                    }

                    cardView.count = card.count

                    // Set the evolution notch stage for the card
                    cardView.evolution = getEvolutionState(chain, nodeIndex, cardIndex)

                    // Set click listener
                    cardView.setOnClickListener {
                        pokemonCardClickListener?.onPokemonCardClicked(cardView, card.card)
                    }

                    actionAdd.setOnClickListener {
                        pokemonCardEditListener?.onPokemonCardAdded(card.card)
                    }

                    actionRemove.setOnClickListener {
                        pokemonCardEditListener?.onPokemonCardRemoved(card.card)
                    }

                    val clp = cardView.layoutParams as MarginLayoutParams
                    clp.setMargins(0, 0, 0, 0)
                    cardView.layoutParams = clp

                    val alp = actionLayout.layoutParams as MarginLayoutParams
                    alp.bottomMargin = dipToPx(24f)
                    actionLayout.layoutParams = alp

                    // Calculate approximate cardWidth
                    if (this@EvolutionChainView.parent is androidx.recyclerview.widget.RecyclerView) {
                        val p = this@EvolutionChainView.parent as androidx.recyclerview.widget.RecyclerView
                        if (p.width > 0) {
                            val lm = p.layoutManager as androidx.recyclerview.widget.GridLayoutManager
                            cardWidth = (p.width - (2 * stageSpacing + (lm.spanCount - 1) * linkSpacing)) / lm.spanCount
                        }
                    }

                    val lp = LayoutParams(cardWidth, LayoutParams.WRAP_CONTENT)
                    lp.marginStart = if (cardIndex == 0 && nodeIndex == 0) {
                        linkSpacing / 2
                    } else if (cardIndex == 0) {
                        linkSpacing
                    } else {
                        nodeSpacing
                    }
                    lp.topMargin = chainSpacing
                    lp.bottomMargin = chainSpacing
                    view.layoutParams = lp

                    if (dragAndDropEnabled) {
                        view.setOnLongClickListener { v ->
                            (v as PokemonCardView).startDrag(true)
                            true
                        }
                    }

                    // Increment the view index
                    viewIndex++
                }
                node = node.next
                nodeIndex++
            }

            // If there are more views than views than what was index, remove those views
            if (childCount > viewIndex) {
                val count = childCount
                (viewIndex until count).forEach { i ->
                    getChildAt(i)?.let { removeView(it) }
                }
            }

        }
    }

    private fun getEvolutionState(chain: EvolutionChain, nodeIndex: Int, cardIndex: Int): PokemonCardView.Evolution {
        val node = chain.nodes[nodeIndex]
        val isFirstNode = nodeIndex == 0
        val isFirstCard = cardIndex == 0
        val isLastCard = cardIndex == node.cards.size - 1
        val hasNextNode = nodeIndex < chain.nodes.size - 1
        if (isFirstNode) {
            if (hasNextNode && isLastCard) {
                return END
            }
        }
        else {
            if (isFirstCard && isLastCard && hasNextNode) {
                return MIDDLE
            }
            else if (isFirstCard) {
                return START
            }
            else if (isLastCard && hasNextNode) {
                return END
            }
        }

        return NONE
    }

    interface OnPokemonCardClickListener {

        fun onPokemonCardClicked(view: PokemonCardView, card: PokemonCard)
    }

    interface OnPokemonEditListener {

        fun onPokemonCardAdded(card: PokemonCard)
        fun onPokemonCardRemoved(card: PokemonCard)
    }
}
