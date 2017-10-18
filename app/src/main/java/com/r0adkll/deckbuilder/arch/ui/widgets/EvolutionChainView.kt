package com.r0adkll.deckbuilder.arch.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.LinearLayout
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.EvolutionChain
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView.Evolution.*
import timber.log.Timber


class EvolutionChainView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val linkPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val linkBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var cardWidth: Int = dipToPx(120f)
    private val linkSpacing: Int = dipToPx(24f)
    private val stageSpacing: Int = dipToPx(16f)
    private val nodeSpacing: Int = dipToPx(4f)
    private val chainSpacing: Int = dipToPx(8f)
    private val linkRadius: Float = dpToPx(8f)

    private var pokemonCardClickListener: OnPokemonCardClickListener? = null

    var evolutionChain: EvolutionChain? = null
        set(value) {
            field = value
            configurePokemonCardViews()
        }


    init {
        orientation = HORIZONTAL
        setWillNotDraw(false)
        cardWidth = (resources.displayMetrics.widthPixels - (2 * stageSpacing + 2 * linkSpacing)) / 3

        linkPaint.color = color(R.color.primaryColor)
        linkPaint.style = Paint.Style.FILL

        linkBarPaint.color = color(R.color.primaryColor)
        linkBarPaint.style = Paint.Style.STROKE
        linkBarPaint.strokeWidth = linkRadius
    }


    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        (0 until childCount).forEach { index ->
            val child = getChildAt(index) as PokemonCardView
            Timber.d("Should render child [${child.evolution}]")
            if (child.evolution == END || child.evolution == MIDDLE) {
                val nextChild = getChildAt(index + 1)?.let { it as PokemonCardView }
                if (nextChild != null && (nextChild.evolution == START || nextChild.evolution == MIDDLE)) {
                    Timber.d("Rendering between children [${child.evolution} to ${nextChild.evolution}]")
                    // Render Link
                    val y = child.top + (child.height / 2f)

                    // Start node
                    val x1 = child.right.toFloat()
                    canvas.drawCircle(x1, y, linkRadius, linkPaint)

                    // End node
                    val x2 = nextChild.left.toFloat()
                    canvas.drawCircle(x2, y, linkRadius, linkPaint)

                    // Render connecting bar
                    canvas.drawLine(x1, y, x2, y, linkBarPaint)
                }
            }
        }
    }


    fun setOnPokemonCardClickListener(listener: (PokemonCard) -> Unit) {
        pokemonCardClickListener = object : OnPokemonCardClickListener {
            override fun onPokemonCardClicked(card: PokemonCard) {
                listener.invoke(card)
            }
        }
    }


    private fun configurePokemonCardViews() {
        removeAllViews()
        evolutionChain?.let { chain ->

            var node = chain.first()
            var nodeIndex = 0
            while(node != null) {
                node.cards.forEachIndexed { cardIndex, card ->
                    // Attempt to find existing view for index
                    var view = getChildAt(nodeIndex + cardIndex)?.let { it as PokemonCardView }
                    if (view == null) {
                        view = PokemonCardView(context)
                        addView(view)
                    }

                    // Only re-apply the card if it is different
                    if (view.card != card) {
                        view.card = card
                    }

                    // Set the evolution notch stage for the card
                    view.evolution = getEvolutionState(chain, nodeIndex, cardIndex)

                    // Set click listener
                    view.setOnClickListener {
                        pokemonCardClickListener?.onPokemonCardClicked(card)
                    }


                    val lp = LayoutParams(cardWidth, LayoutParams.WRAP_CONTENT)
                    lp.marginStart = if (cardIndex == 0 && nodeIndex == 0) {
                        stageSpacing
                    } else if (cardIndex == 0) {
                        linkSpacing
                    } else {
                        nodeSpacing
                    }
                    lp.topMargin = chainSpacing
                    lp.bottomMargin = chainSpacing
                    view.layoutParams = lp
                }
                node = node.next
                nodeIndex++
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

        fun onPokemonCardClicked(card: PokemonCard)
    }
}