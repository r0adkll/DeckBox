package com.r0adkll.deckbuilder.arch.ui.features.playtest.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ViewGroup
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.r0adkll.deckbuilder.arch.domain.features.playtest.Board
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.util.extensions.forEach
import com.r0adkll.deckbuilder.util.extensions.forEachAs
import com.r0adkll.deckbuilder.util.extensions.forEachIndexed
import com.r0adkll.deckbuilder.util.extensions.layout
import timber.log.Timber
import kotlin.math.roundToInt


class CardStackView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        cards: List<Board.Card> = emptyList(),
        startFaceDown: Boolean = false
) : ViewGroup(context, attrs, defStyleAttr) {

    var isFaceDown: Boolean = startFaceDown
        set(value) {
            if (field != value) {
                field = value
                forEachAs<BoardCardView> {
                    it.isFaceDown = value
                }
            }
        }

    var cards: List<Board.Card> = cards
        set(value) {
            field = value
            balanceCardViews()
        }

    /**
     * The maximum number of cards that this view can offset to give a stacked appearance
     */
    var offsetLimit = 3

    /**
     * Enable or disable debugging of this view via logging and debug rendering
     */
    var debug = false
        set(value) {
            field = value
            invalidate()
        }

    private val debugPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = dpToPx(1f)
    }

    private val cardOffsetX = dipToPx(3f)
    private val cardOffsetY = dipToPx(3f)

    init {
        setWillNotDraw(false)
        balanceCardViews()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specWidth: Int
        val specHeight: Int
        if (parent is BoardView) {
            val boardView = parent as BoardView
            specWidth = boardView.elementWidth.roundToInt()
            specHeight = boardView.elementHeight.roundToInt()
        } else {
            // Get the requested sizing of this view, must be added to parent with
            specWidth = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec) /* Parent Width*/
            specHeight = (specWidth * PokemonCardView.RATIO).roundToInt() /* Parent Height */
        }

        // Configure padding needed for # of children
        val offsetX = (childCount.coerceAtMost(offsetLimit) - 1) * cardOffsetX
        val offsetY = (childCount.coerceAtMost(offsetLimit) - 1) * cardOffsetY

        if (debug) Timber.i("onMeasure(w=$specWidth, h=$specHeight, offsetX=$offsetX, offsetY=$offsetY)")

        // Set the measure of the stack
        setMeasuredDimension(specWidth + offsetX, specHeight + offsetY)

        val elementWidthMeasureSpec = MeasureSpec.makeMeasureSpec(specWidth, MeasureSpec.EXACTLY)
        forEachIndexed { index, child ->
            val childHeightMeasureSpec = getChildMeasureSpec(
                    widthMeasureSpec,
                    paddingTop + paddingBottom,
                    LayoutParams.WRAP_CONTENT
            )
            child.measure(elementWidthMeasureSpec, childHeightMeasureSpec)
            if (debug) Timber.i("onChild($index)::onMeasure(w=${child.measuredWidth}, h=${child.measuredHeight})")
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val leftPos = paddingLeft
        val topPos = paddingTop

        forEachIndexed { index, child ->
            val offsetX = index.coerceAtMost(offsetLimit - 1) * cardOffsetX
            val offsetY = index.coerceAtMost(offsetLimit - 1) * cardOffsetY
            child.layout(leftPos + offsetX, topPos + offsetY)
            if (debug) Timber.i("onLayoutChild($index, w=${leftPos + offsetX}, h=${topPos + offsetY})")
        }
    }

    override fun onDrawForeground(canvas: Canvas) {
        super.onDrawForeground(canvas)
        if (debug) {
            // Render bounds
            canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), debugPaint)

            // Render Child markers
            forEach {
                val x = it.left.toFloat()
                val y = it.top.toFloat()
                canvas.drawLine(x, y, x + dpToPx(16f), y, debugPaint)
                canvas.drawLine(x, y, x, y + dpToPx(16f), debugPaint)
            }
        }
    }

    private fun balanceCardViews() {
        if (cards.isNotEmpty()) {
            cards.forEachIndexed { index, card ->
                var childView = getChildAt(index) as? BoardCardView
                if (childView != null) {
                    childView.isFaceDown = isFaceDown
                    childView.card = card
                } else {
                    childView = BoardCardView(context, card = card, startFaceDown = isFaceDown)
                    addView(childView)
                }
            }

            if (cards.size > childCount) {
                val extraCount = cards.size - childCount
                for (i in 0 until extraCount) {
                    removeViewAt(childCount - 1)
                }
            }
        } else if (childCount > 0) {
            removeAllViews()
        }
    }
}