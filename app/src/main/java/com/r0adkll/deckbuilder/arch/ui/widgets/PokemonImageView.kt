package com.r0adkll.deckbuilder.arch.ui.widgets

import android.content.Context
import android.util.AttributeSet

class PokemonImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : ForegroundImageView(context, attrs, defStyle) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = Math.round(width * PokemonCardView.RATIO)
        setMeasuredDimension(width, height)
    }
}
