package com.r0adkll.deckbuilder.arch.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import kotlin.math.roundToInt


class TestResultView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    var progress: Float = 0f
    var pokemonCard: PokemonCard? = null

    private val resultPercentage = TextView(context)
    private val title = TextView(context)
    private val card = ImageView(context)

    private val defaultHeight = dipToPx(72f)
    private val defaultPadding = dipToPx(20f)
    private val cardMargin = dipToPx(16f)
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)


    init {
        setPaddingRelative(defaultPadding, 0, defaultPadding, 0)
        resultPercentage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        resultPercentage.typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        resultPercentage.setTextColor(color(R.color.white))
        resultPercentage.text = "${(progress * 100f).roundToInt()}%"
        addView(resultPercentage)

        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        title.typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        title.setTextColor(color(R.color.white))

        addView(card, )

        backgroundPaint.color = color(R.color.primaryColor)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, defaultHeight)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        // Layout the result percentage
        val rpX = cardMargin + paddingStart
        val rpY = (measuredHeight / 2) - (resultPercentage.height / 2)
        resultPercentage.layout(rpX, rpY, rpX + resultPercentage.width, rpY + resultPercentage.height)

        // Layout Card
        val realWidth = width - (cardMargin + paddingStart + paddingEnd + cardMargin + resultPercentage.width)

    }
}