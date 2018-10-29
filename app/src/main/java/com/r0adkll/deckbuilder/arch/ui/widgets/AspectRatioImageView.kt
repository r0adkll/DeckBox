package com.r0adkll.deckbuilder.arch.ui.widgets

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import android.util.AttributeSet
import com.r0adkll.deckbuilder.R


class AspectRatioImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    // The Ratio Type
    var ratioType = RATIO_WIDTH


    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView, defStyleAttr, 0)
        a?.let {
            ratioType = a.getInt(R.styleable.AspectRatioImageView_ratioType, RATIO_WIDTH)
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (drawable != null) {
            if (ratioType == RATIO_WIDTH) {
                val width = MeasureSpec.getSize(widthMeasureSpec)
                val height = Math.round(width * (drawable.intrinsicHeight.toFloat() / drawable.intrinsicWidth.toFloat()))
                setMeasuredDimension(width, height)
            } else if (ratioType == RATIO_HEIGHT) {
                val height = MeasureSpec.getSize(heightMeasureSpec)
                val width = Math.round(height * (drawable.intrinsicWidth.toFloat() / drawable.intrinsicHeight.toFloat()))
                setMeasuredDimension(width, height)
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    companion object {
        private const val RATIO_WIDTH = 0
        private const val RATIO_HEIGHT = 1
    }
}