@file:Suppress("MagicNumber")

package com.r0adkll.deckbuilder.arch.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.ftinc.kit.extensions.color
import com.ftinc.kit.extensions.dip
import com.ftinc.kit.extensions.dp
import com.r0adkll.deckbuilder.R

class DotPageIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), ViewPager.OnPageChangeListener {

    var index: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    var max: Int = DEFAULT_MAX
        set(value) {
            field = value
            invalidate()
        }

    var unselectedColor: Int = R.color.white30
        set(value) {
            field = value
            invalidate()
        }

    var selectedColor: Int = R.color.white
        set(value) {
            field = value
            invalidate()
        }

    val size: Int = dip(8)
    val spacing: Float = dp(8)

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var pager: ViewPager? = null

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        pager?.removeOnPageChangeListener(this)
        pager = null
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = (max * size) + ((max - 1) * spacing)
        setMeasuredDimension(width.toInt(), size)
    }

    override fun onDraw(canvas: Canvas?) {
        (0..max).forEach {
            val isCurrent = it == index
            val colorRes = if (isCurrent) selectedColor else unselectedColor

            // Setup Paint
            paint.color = color(colorRes)

            val y = measuredHeight / 2f
            val x = paddingLeft + ((it * size) + (it * spacing))
            val radius = size / 2f

            canvas?.drawCircle(x + radius, y, radius, paint)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        index = position
    }

    fun setupWithViewPager(pager: ViewPager) {
        this.pager = pager
        max = pager.adapter?.count ?: 0
        pager.addOnPageChangeListener(this)
    }

    companion object {
        const val DEFAULT_MAX = 3
    }
}
