package com.r0adkll.deckbuilder.arch.ui.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.ftinc.kit.kotlin.extensions.spToPx
import com.r0adkll.deckbuilder.R

/**
 * TODO: NAME TO CHANGE
 */
class ArenaPlaymatView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val silhouettePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val silhouetteTextPaint: Paint = Paint(Paint.LINEAR_TEXT_FLAG)
    private val cardPunchPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val pikaCoinImage: Bitmap

    private val benchMargin: Float = dpToPx(24f)
    private val silhouetteMarginInside: Float = dpToPx(16f)
    private val cardRadius = dpToPx(4f)
    private var silhouetteWidth: Float = 0f
    private var silhouetteHeight: Float = 0f


    init {
        paint.color = color(R.color.playmat)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = dpToPx(8f)
        paint.strokeCap = Paint.Cap.ROUND

        silhouettePaint.color = color(R.color.playmat)
        silhouettePaint.style = Paint.Style.STROKE
        silhouettePaint.strokeWidth = dpToPx(4f)
        silhouettePaint.strokeCap = Paint.Cap.ROUND

        silhouetteTextPaint.color = color(R.color.playmat)
        silhouetteTextPaint.textSize = spToPx(14f)
        silhouetteTextPaint.typeface = Typeface.DEFAULT_BOLD
        silhouetteTextPaint.textAlign = Paint.Align.CENTER

        cardPunchPaint.color = Color.WHITE
        cardPunchPaint.style = Paint.Style.FILL_AND_STROKE
        cardPunchPaint.strokeWidth = dpToPx(4f)

        pikaCoinImage = BitmapFactory.decodeResource(resources, R.drawable.ic_pika_coin)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        silhouetteWidth = (measuredWidth.toFloat() -  ((benchMargin * 2)  +  silhouetteMarginInside * 4)) / 5f
        silhouetteHeight = silhouetteWidth * PokemonCardView.RATIO
    }


    override fun onDraw(canvas: Canvas) {
        /*
         * Draw the Active Arena
         */
        drawPokeBall(canvas)

        /*
         * Draw the Benches
         */
        val baseX = benchMargin
        val baseY = measuredHeight - (benchMargin + silhouetteHeight)
        (0 until 5).forEach {
            val offsetX = (it * silhouetteWidth) + (it * silhouetteMarginInside)
            drawCardSilhouette(canvas, baseX + offsetX, baseY)
        }

        (1 until 6).forEach {
            val offsetX = measuredWidth - ((it * silhouetteWidth) + ((it - 1) * silhouetteMarginInside))
            drawCardSilhouette(canvas, offsetX - benchMargin, benchMargin)
        }

        /*
         * Draw the Deck & Discard
         */
        val deckX = baseX + (4 * silhouetteWidth) + (4 * silhouetteMarginInside)
        val deckY = baseY - ((silhouetteHeight + silhouetteMarginInside) * 2) + (silhouetteMarginInside / 2)
        drawCardSilhouette(canvas, deckX, deckY, "DECK")
        drawCardSilhouette(canvas, deckX, deckY + (silhouetteHeight + silhouetteMarginInside/2), "DISC.")

        val opponentDeckX = benchMargin
        val opponentDeckY = benchMargin + silhouetteHeight + silhouetteMarginInside + silhouetteHeight + (silhouetteMarginInside / 2f)
        drawCardSilhouette(canvas, opponentDeckX, opponentDeckY, "DECK")
        drawCardSilhouette(canvas, opponentDeckX, opponentDeckY - (silhouetteHeight + silhouetteMarginInside/2f), "DISC.")
    }


    private fun drawCardSilhouette(canvas: Canvas, x: Float, y: Float, text: String? = null) {
        canvas.drawRoundRect(x, y, x + silhouetteWidth, y + silhouetteHeight, cardRadius, cardRadius, silhouettePaint)
        if (text != null) {
            canvas.drawText(text, x + silhouetteWidth / 2f, (y + silhouetteHeight / 2f), silhouetteTextPaint)
        }
    }


    private fun drawPokeBall(canvas: Canvas) {
        val centerX = measuredWidth / 2f
        val centerY = measuredHeight / 2f
        val radius = measuredWidth / 5f
        val innerRadius = radius * .40f

        canvas.drawCircle(centerX, centerY, innerRadius, paint)
        canvas.drawCircle(centerX, centerY, radius, paint)
        canvas.drawLine(centerX - radius, centerY, centerX - innerRadius, centerY, paint)
        canvas.drawLine(centerX + innerRadius, centerY, centerX + radius, centerY, paint)

        val activeX = centerX - (silhouetteWidth / 2f)
        val activeY = centerY + (innerRadius  / 2f)
        val pad = dpToPx(2f)
        canvas.drawRoundRect(activeX - pad, activeY - pad,
                activeX + silhouetteWidth + pad,
                activeY + silhouetteHeight + pad, cardRadius, cardRadius, cardPunchPaint)
        drawCardSilhouette(canvas, activeX, activeY)

        val opponentActiveY = centerY - (innerRadius / 2f) - silhouetteHeight
        canvas.drawRoundRect(activeX - pad, opponentActiveY - pad,
                activeX + silhouetteWidth + pad,
                opponentActiveY + silhouetteHeight + pad, cardRadius, cardRadius, cardPunchPaint)
        drawCardSilhouette(canvas, activeX, opponentActiveY)
    }
}