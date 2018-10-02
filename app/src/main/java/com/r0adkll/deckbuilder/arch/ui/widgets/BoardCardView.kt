package com.r0adkll.deckbuilder.arch.ui.widgets

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.r0adkll.deckbuilder.arch.domain.features.playtest.Board


class BoardCardView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {


    /**
     * The [Board.Card] State that this view will be rendering
     */
    var card: Board.Card? = null
        set(value) {
            field = value
            inflateCard()
        }


    private fun inflateCard() {
        if (card != null) {

        }
    }
}