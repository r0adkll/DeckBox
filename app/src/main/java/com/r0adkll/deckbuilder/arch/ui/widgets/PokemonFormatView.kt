package com.r0adkll.deckbuilder.arch.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import com.ftinc.kit.extensions.color
import com.ftinc.kit.extensions.dp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.Format

/**
 * Custom view for renderering all the different validation formats of tournament play
 * i.e. Standard, Expanded, Unlimited, Theme (this is a special one
 */
class PokemonFormatView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var format: Format = Format.STANDARD
        set(value) {
            field = value
            invalidate()
        }
    var strokeWidth: Float = dp(4)
        set(value) {
            field = value
            paint.strokeWidth = value
            invalidate()
        }
    var strokeColor: Int = color(R.color.format_stroke)
        set(value) {
            field = value
            invalidate()
        }
    var fillColor: Int = color(R.color.white)
        set(value) {
            field = value
            invalidate()
        }

    var ringPadding = dp(4)
    var ringRadius = 0f
    var nodeRadius = dp(4)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.color = strokeColor
        paint.strokeWidth = strokeWidth
        paint.style = Paint.Style.STROKE

        val a = context.obtainStyledAttributes(attrs, R.styleable.PokemonFormatView, defStyleAttr, 0)
        val formatValue = a.getInteger(R.styleable.PokemonFormatView_format, 0)
        format = Format.values()[formatValue]

        strokeWidth = a.getDimension(R.styleable.PokemonFormatView_strokeWidth, strokeWidth)
        strokeColor = a.getColor(R.styleable.PokemonFormatView_strokeColor, strokeColor)
        fillColor = a.getColor(R.styleable.PokemonFormatView_fillColor, fillColor)
        ringPadding = a.getDimension(R.styleable.PokemonFormatView_ringPadding, ringPadding)

        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth)
        val viewPadding = Math.max(paddingStart + paddingEnd, paddingTop + paddingBottom)
        nodeRadius = ((measuredWidth - viewPadding) / 3f) / 2f
        ringRadius = ((measuredWidth - viewPadding) / 2f) - ringPadding
    }

    override fun onDraw(canvas: Canvas) {
        drawRing(canvas)
        when(format) {
            Format.STANDARD -> drawNodes(canvas, 135f, 315f)
            Format.EXPANDED -> drawNodes(canvas, 0f, 120f, 240f)
            Format.UNLIMITED -> drawNodes(canvas, 0f, 72f, 144f, 216f, 288f)
            Format.LEGACY -> drawNodes(canvas, 45f, 135f, 225f, 315f)
            Format.THEME -> { /* Do Nothing */ }
        }
    }

    private fun drawNodes(canvas: Canvas, vararg angles: Float) {
        val cx = measuredWidth / 2f
        val cy = measuredHeight / 2f
        angles.forEach { angle ->
            val correctedAngle = angle - 180f
            val point = pointOnCircle(correctedAngle, ringRadius)
            drawCircle(canvas, cx + point.x, cy + point.y, nodeRadius)
        }
    }

    private fun drawRing(canvas: Canvas) {
        val cx = measuredWidth / 2f
        val cy = measuredHeight / 2f
        drawCircle(canvas, cx, cy, ringRadius)
    }

    private fun drawCircle(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        paint.style = Paint.Style.FILL
        paint.color = fillColor
        canvas.drawCircle(cx, cy, radius, paint)
        paint.style = Paint.Style.STROKE
        paint.color = strokeColor
        canvas.drawCircle(cx, cy, radius, paint)
    }

    private fun pointOnCircle(angleInDegrees: Float, radius: Float): PointF {
        val angleInRadians = Math.toRadians(angleInDegrees.toDouble())
        val x = radius * Math.sin(angleInRadians)
        val y = radius * Math.cos(angleInRadians)
        return PointF(x.toFloat(), y.toFloat())
    }
}
