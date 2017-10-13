package com.r0adkll.deckbuilder.arch.ui.widgets


import android.content.Context
import android.graphics.*
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.r0adkll.deckbuilder.R


class PokemonCardView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    val paint: Paint by lazy {
        val p = Paint()
        p.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        p
    }

    private val radius = dpToPx(8f)

    init {
        setImageResource(R.drawable.pokemon_card_back)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = Math.round(width * RATIO)
        setMeasuredDimension(width, height)
    }


    override fun onDraw(canvas: Canvas?) {
        val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        val c = Canvas(bitmap)
        super.onDraw(c)
        c?.drawRoundRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), radius, radius, paint)
        canvas?.drawBitmap(bitmap, 0f, 0f, null)
    }


    companion object {
        const val RATIO = 1.3959183673f
    }
}