package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.line


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView


class EvolutionLineItemDecoration(
        val context: Context,
        val adapter: EvolutionLineAdapter
) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    private val linkPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val linkBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val nodeSpacing: Int = context.dipToPx(4f)
    private val linkSpacing: Int = context.dipToPx(24f)
    private val linkRadius: Float = context.dpToPx(8f)


    init {
        linkPaint.color = context.color(R.color.primaryColor)
        linkPaint.style = Paint.Style.FILL

        linkBarPaint.color = context.color(R.color.primaryColor)
        linkBarPaint.style = Paint.Style.STROKE
        linkBarPaint.strokeWidth = linkRadius
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        val adapterPosition = parent.getChildAdapterPosition(view)
        val evolutionState = adapter.getEvolutionState(adapterPosition)

        val spacing = if (evolutionState.cardIndex == 0 && evolutionState.nodeIndex == 0) {
            0
        } else if (evolutionState.cardIndex == 0) {
            linkSpacing
        } else {
            nodeSpacing
        }

        outRect.set(spacing, 0, 0, 0)
    }


    override fun onDrawOver(c: Canvas, parent: androidx.recyclerview.widget.RecyclerView, s: androidx.recyclerview.widget.RecyclerView.State) {
        val adapter = parent.adapter
        adapter?.let { it ->
            (0 until it.itemCount).forEach { index ->
                val state = this.adapter.getEvolutionState(index)
                if (state.evolution == PokemonCardView.Evolution.END || state.evolution == PokemonCardView.Evolution.MIDDLE) {
                    val child = parent.getChildAt(index)
                    val nextChild = parent.getChildAt(index + 1)?.findViewById<PokemonCardView>(R.id.card)
                    if (nextChild != null && (nextChild.evolution == PokemonCardView.Evolution.START || nextChild.evolution == PokemonCardView.Evolution.MIDDLE)) {
                        // Render Link
                        val y = child.top + (child.height / 2f)

                        // Start node
                        val x1 = child.right.toFloat()
                        c.drawCircle(x1, y, linkRadius, linkPaint)

                        // End node
                        val x2 = x1 + linkSpacing //nextChild.left.toFloat()
                        c.drawCircle(x2, y, linkRadius, linkPaint)

                        // Render connecting bar
                        c.drawLine(x1, y, x2, y, linkBarPaint)
                    }
                }
            }
        }
    }
}