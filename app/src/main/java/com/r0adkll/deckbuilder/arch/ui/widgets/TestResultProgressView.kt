package com.r0adkll.deckbuilder.arch.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.ftinc.kit.kotlin.extensions.spToPx
import com.r0adkll.deckbuilder.R

class TestResultProgressView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var percentage: Float = 0f
    var maxPercentage: Float = 100f
    var isMulligan: Boolean = false
        set(value) {
            field = value
            backgroundPaint.color = if (value) color(R.color.grey_500) else color(R.color.primaryColor)
        }

    private val cardRadius = dpToPx(8f)
    private val mulliganMargin = dpToPx(16f)
    private val defaultHeight = dipToPx(96f)
    private val percentWidth = dipToPx(88f)
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mulliganText: StaticLayout

    init {
        backgroundPaint.color = color(R.color.primaryColor)

        val textPaint = TextPaint(Paint.LINEAR_TEXT_FLAG)
        textPaint.color = Color.WHITE
        textPaint.textSize = spToPx(20f)
        textPaint.typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        val width = textPaint.measureText("Mulligan")

        // This should be updated when we move the minSdk to 23+
        @Suppress("DEPRECATION")
        mulliganText = StaticLayout("Mulligan", textPaint, width.toInt(), Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)

        if (isInEditMode) {
            percentage = 100f
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val offset = (if (isMulligan) mulliganText.width.toFloat() else (defaultHeight / PokemonCardView.RATIO)) + percentWidth

        val workingWidth = measuredWidth - offset - (cardRadius * 2f)
        val workingPercent = percentage / maxPercentage

        val percentageWidth = (workingWidth * workingPercent).toInt() + offset.toInt()
        setMeasuredDimension(percentageWidth, defaultHeight)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), cardRadius, cardRadius, backgroundPaint)
        canvas.drawRect(0f, 0f, cardRadius, height.toFloat(), backgroundPaint)
        if (isMulligan) {
            val dx = (measuredWidth - mulliganText.width).toFloat() - mulliganMargin
            val dy = (measuredHeight / 2f) - (mulliganText.height.toFloat() / 2f)
            val count = canvas.save()
            canvas.translate(dx, dy)
            mulliganText.draw(canvas)
            canvas.restoreToCount(count)
        }
    }
}
