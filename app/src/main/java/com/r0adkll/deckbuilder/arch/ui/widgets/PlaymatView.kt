package com.r0adkll.deckbuilder.arch.ui.widgets

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.ftinc.kit.extensions.color
import com.ftinc.kit.extensions.dp
import com.ftinc.kit.extensions.sp
import com.r0adkll.deckbuilder.R

class PlaymatView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val silhouettePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val silhouetteTextPaint: Paint = Paint(Paint.LINEAR_TEXT_FLAG)

    private val pikaCoinImage: Bitmap
    private val diceClusterImage: Bitmap

    private val silhouetteMargin: Float = dp(32)
    private val silhouetteMarginInside: Float = dp(16)
    private val cardRadius = dp(4)
    private var silhouetteWidth: Float = 0f
    private var silhouetteHeight: Float = 0f

    init {
        paint.color = color(R.color.playmat)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = dp(8)
        paint.strokeCap = Paint.Cap.ROUND

        silhouettePaint.color = color(R.color.playmat)
        silhouettePaint.style = Paint.Style.STROKE
        silhouettePaint.strokeWidth = dp(6)
        silhouettePaint.strokeCap = Paint.Cap.ROUND

        silhouetteTextPaint.color = color(R.color.playmat)
        silhouetteTextPaint.textSize = sp(14)
        silhouetteTextPaint.typeface = Typeface.DEFAULT_BOLD
        silhouetteTextPaint.textAlign = Paint.Align.CENTER

        pikaCoinImage = BitmapFactory.decodeResource(resources, R.drawable.ic_pika_coin)
        diceClusterImage = BitmapFactory.decodeResource(resources, R.drawable.ic_dice_cluster)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        silhouetteWidth = (measuredWidth.toFloat() -  ((silhouetteMargin * 2)  +  silhouetteMarginInside * 4)) / 5f
        silhouetteHeight = silhouetteWidth * PokemonCardView.RATIO

    }

    override fun onDraw(canvas: Canvas) {
        // Draw Pokeball
        drawPokeBall(canvas)

        // Draw all the silhouttes
        val baseX = silhouetteMargin
        val baseY = measuredHeight - (silhouetteMargin + silhouetteHeight)
        (0 until 5).forEach {
            val offsetX = (it * silhouetteWidth) + (it * silhouetteMarginInside)
            drawCardSilhouette(canvas, baseX + offsetX, baseY)
        }

        // Draw deck pile
        val deckX = baseX + (4 * silhouetteWidth) + (4 * silhouetteMarginInside)
        val deckY = baseY - (silhouetteHeight + silhouetteMarginInside)
        drawCardSilhouette(canvas, deckX, deckY, "DECK")

        // Draw pika coin
        val count = canvas.save()
        val coinX = (measuredWidth - (silhouetteMargin + pikaCoinImage.width)) - dp(24)
        val coinY = deckY - pikaCoinImage.height - dp(56)
        canvas.rotate(-30f, coinX, coinY)
        canvas.drawBitmap(pikaCoinImage, coinX, coinY, null)
        canvas.restoreToCount(count)

        // Draw dice cluster
        val diceX = silhouetteMargin + dp(16)
        val diceY = (baseY - diceClusterImage.height) - dp(64)
        canvas.drawBitmap(diceClusterImage, diceX, diceY, null)
    }

    private fun drawCardSilhouette(canvas: Canvas, x: Float, y: Float, text: String? = null) {
        canvas.drawRoundRect(x, y, x + silhouetteWidth, y + silhouetteHeight, cardRadius, cardRadius, silhouettePaint)
        if (text != null) {
            canvas.drawText(text, x + silhouetteWidth / 2f, (y + silhouetteHeight / 2f), silhouetteTextPaint)
        }
    }

    private fun drawPokeBall(canvas: Canvas) {
        val centerX = measuredWidth / 2f
        val radius = measuredWidth / 4f
        val innerRadius = radius * .40f

        canvas.drawCircle(centerX, 0f, innerRadius, paint)
        canvas.drawCircle(centerX, 0f, radius, paint)
        canvas.drawLine(centerX - radius, 0f, centerX - innerRadius, 0f, paint)
        canvas.drawLine(centerX + innerRadius, 0f, centerX + radius, 0f, paint)
    }
}
