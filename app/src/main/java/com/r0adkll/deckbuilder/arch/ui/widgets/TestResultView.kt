@file:Suppress("MagicNumber")

package com.r0adkll.deckbuilder.arch.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.ftinc.kit.extensions.color
import com.ftinc.kit.extensions.dip
import com.ftinc.kit.extensions.dp
import com.r0adkll.deckbuilder.R
import kotlin.math.roundToInt

class TestResultView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    var percentage: Float = 0f
        @SuppressLint("SetTextI18n")
        set(value) {
            field = value
            resultPercentage.text = "${(percentage * 100f).roundToInt()}%"
            requestLayout()
        }

    var cardImageUrl: String? = null
        set(value) {
            field = value
            Glide.with(this)
                .load(field)
                .into(card)
        }

    var isMulligan: Boolean = false
        set(value) {
            field = value
            card.isGone = value
            title.isVisible = value
            backgroundPaint.color = color(if (value) R.color.grey_300 else R.color.primaryColor)
        }

    var text: String = context.getString(R.string.mulligan)
        set(value) {
            field = value
            title.text = value
        }

    private val resultPercentage = TextView(context)
    private val title = TextView(context)
    private val card = ImageView(context)

    private val defaultHeight = dip(72)
    private val defaultPadding = dip(20)
    private val cardMargin = dip(16)
    private val cardRadius = dp(8)
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        setWillNotDraw(false)
        setPaddingRelative(defaultPadding, 0, defaultPadding, 0)
        resultPercentage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        resultPercentage.typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        resultPercentage.setTextColor(color(R.color.white))
        @SuppressLint("SetTextI18n")
        resultPercentage.text = "${(percentage * 100f).roundToInt()}%"
        addView(resultPercentage)

        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        title.typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        title.setTextColor(color(R.color.white))
        title.isInvisible = true
        addView(title)

        val w = defaultHeight / PokemonCardView.RATIO
        val cardLp = LayoutParams(w.toInt(), defaultHeight)
        addView(card, cardLp)

        backgroundPaint.color = color(R.color.primaryColor)

        if (isInEditMode) {
            percentage = 50f
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, defaultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        // Layout the result percentage
        val rpX = cardMargin + paddingStart
        val rpY = (measuredHeight / 2) - (resultPercentage.height / 2)
        resultPercentage.layout(rpX, rpY, rpX + resultPercentage.width, rpY + resultPercentage.height)

        // Layout Card
        val realWidth = measuredWidth - (paddingStart + resultPercentage.width + cardMargin + paddingEnd)
        val cX = paddingStart + resultPercentage.width + cardMargin + (realWidth * (percentage / 100f))

        card.layout(cX.toInt(), 0, cX.toInt() + card.measuredWidth, defaultHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawRoundRect(0f, 0f, card.right.toFloat(), defaultHeight.toFloat(), cardRadius, cardRadius,
            backgroundPaint)
        canvas?.drawRect(0f, 0f, cardRadius, defaultHeight.toFloat(), backgroundPaint)
        super.onDraw(canvas)
    }
}
