package com.r0adkll.deckbuilder.arch.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.TextView
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.r0adkll.deckbuilder.R

class SeekBarIndicatorView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), SeekBar.OnSeekBarChangeListener {

    private val indicator: TextView = TextView(context)
    var valueFormatter: ValueFormatter = DefaultValueFormatter()
    var seekBarChangeListener: SeekBar.OnSeekBarChangeListener? = null

    init {
        indicator.gravity = Gravity.CENTER
        indicator.setPadding(0, 0, 0, dipToPx(8f))
        indicator.setTextColor(color(R.color.white))
        indicator.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        indicator.setBackgroundResource(R.drawable.ic_value_bubble)

        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        addView(indicator, lp)

        if (isInEditMode) {
            @SuppressLint("SetTextI18n")
            indicator.text = "50"
            post { syncToProgress(50, 100) }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        seekBarChangeListener?.onProgressChanged(seekBar, progress, fromUser)
        syncToProgress(progress, seekBar.max)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        seekBarChangeListener?.onStartTrackingTouch(seekBar)
        indicator.elevation = 0f
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        seekBarChangeListener?.onStopTrackingTouch(seekBar)
        indicator.elevation = dpToPx(4f)
        syncToProgress(seekBar.progress, seekBar.max)
    }

    fun setOnSeekBarChangeListener(listener: SeekBar.OnSeekBarChangeListener) {
        seekBarChangeListener = listener
    }

    private fun syncToProgress(progress: Int, max: Int) {
        val percent = progress.toFloat() / max.toFloat()
        val width = (measuredWidth - paddingStart - paddingEnd) - indicator.width
        val translationX = ((width * percent) + paddingStart) - dpToPx(0.5f)
        indicator.translationX = translationX
        indicator.text = valueFormatter.format(progress)
    }

    interface ValueFormatter {

        fun format(progress: Int): CharSequence
    }

    class DefaultValueFormatter: ValueFormatter {
        override fun format(progress: Int): CharSequence {
            return progress.toString()
        }
    }
}
