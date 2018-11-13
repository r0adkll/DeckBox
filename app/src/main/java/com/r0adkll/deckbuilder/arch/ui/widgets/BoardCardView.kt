package com.r0adkll.deckbuilder.arch.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.arch.domain.features.playtest.Board


class BoardCardView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BezelImageView(context, attrs, defStyleAttr) {


    /**
     * The [Board.Card] State that this view will be rendering
     */
    var card: Board.Card? = null
        set(value) {
            field = value
            inflateCard()
        }

    // Initialize the mask drawable
    init {
        val radius = dpToPx(8f)
        val shape = RoundRectShape(floatArrayOf(
                radius, radius,
                radius, radius,
                radius, radius,
                radius, radius
        ), null, null)
        val dr = ShapeDrawable(shape)
        dr.paint.color = Color.BLACK
        maskDrawable = dr
    }

    private fun inflateCard() {
        card?.let { c ->
            // Load the pokemon card image for the card that is on top of the stack
            GlideApp.with(this)
                    .load(c.pokemons.peek()?.imageUrl)
                    .into(this)

            // Draw Energy

            // Draw Tool(s)

            // Draw Damage

            // Draw Conditions
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Render Card items
        card?.let { c ->



        }

    }
}