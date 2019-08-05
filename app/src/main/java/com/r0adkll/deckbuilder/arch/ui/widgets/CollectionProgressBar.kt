package com.r0adkll.deckbuilder.arch.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.r0adkll.deckbuilder.R


class CollectionProgressBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var trackColor: Int = Color.WHITE
        set(value) {
            field = value
            invalidate()
        }

    var progressColor: Int = Color.BLUE
        set(value) {
            field = value
            invalidate()
        }

    var borderColor: Int = Color.WHITE
        set(value) {
            field = value
            invalidate()
        }

    var borderWidth: Float = dpToPx(1f)
        set(value) {
            field = value
            paint.strokeWidth = value
            invalidate()
        }

    var progress: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val renderBounds = RectF()


    init {
        paint.strokeWidth = dpToPx(1f)

        val a = context.obtainStyledAttributes(attrs, R.styleable.CollectionProgressBar, defStyleAttr, 0)
        a?.let {
            trackColor = it.getColor(R.styleable.CollectionProgressBar_trackColor, color(R.color.white50))
            progressColor = it.getColor(R.styleable.CollectionProgressBar_progressColor, color(R.color.secondaryColor))
            borderColor = it.getColor(R.styleable.CollectionProgressBar_borderColor, Color.WHITE)
            borderWidth = it.getDimension(R.styleable.CollectionProgressBar_borderWidth, dpToPx(1f))
            a.recycle()
        }

        if (isInEditMode) {
            progress = .33f
        }
    }

    override fun onDraw(canvas: Canvas) {
        val radius = (height - (paddingTop + paddingBottom)) / 2f

        // Render Track
        paint.color = trackColor
        paint.style = Paint.Style.FILL
        canvas.drawRoundRect(getRenderBounds(), radius, radius, paint)

        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        canvas.drawRoundRect(getRenderBounds(), radius, radius, paint)

        if (progress > 0f) {
            // Render Progress
            paint.color = progressColor
            paint.style = Paint.Style.FILL
            canvas.drawRoundRect(getRenderBounds(progress), radius, radius, paint)

            paint.color = borderColor
            paint.style = Paint.Style.STROKE
            canvas.drawRoundRect(getRenderBounds(progress), radius, radius, paint)
        }
    }

    private fun getRenderBounds(progress: Float = 1f): RectF {
        val left = paddingStart.toFloat()
        val top = paddingTop.toFloat()
        val bottom = (height - (paddingTop + paddingBottom)).toFloat()
        val right = (width - (paddingStart + paddingEnd)) * progress
        renderBounds.set(left, top, right, bottom)
        return renderBounds
    }
}