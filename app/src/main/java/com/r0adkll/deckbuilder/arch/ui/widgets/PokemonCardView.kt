package com.r0adkll.deckbuilder.arch.ui.widgets


import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.r0adkll.deckbuilder.R


class PokemonCardView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    init {
        setImageResource(R.drawable.pokemon_card_back)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = Math.round(width * RATIO)
        setMeasuredDimension(width, height)
    }


    companion object {
        const val RATIO = 1.3959183673f
    }
}