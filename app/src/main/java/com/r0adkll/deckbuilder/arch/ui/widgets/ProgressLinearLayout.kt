package com.r0adkll.deckbuilder.arch.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.LinearLayout
import com.ftinc.kit.extensions.color
import com.r0adkll.deckbuilder.R

class ProgressLinearLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var progress: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val paint = Paint()

    init {
        setWillNotDraw(false)

        paint.color = color(R.color.secondaryColor)
        paint.style = Paint.Style.FILL

        if (isInEditMode) progress = .25f
    }

    override fun onDraw(canvas: Canvas) {
        val progressHeight = height * progress
        canvas.drawRect(0f, height - progressHeight, width.toFloat(), height.toFloat(), paint)
        super.onDraw(canvas)
    }
}
