package com.r0adkll.deckbuilder.arch.ui.widgets


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.ftinc.kit.kotlin.extensions.spToPx
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard


class PokemonCardView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : ForegroundImageView(context, attrs, defStyle) {

    private val blackPaint: Paint = Paint()
    private val maskedPaint: Paint = Paint()
    private val punchPaint: Paint = Paint()
    private val countPaint: Paint = Paint()
    private val countTextPaint: TextPaint = TextPaint(TextPaint.LINEAR_TEXT_FLAG)

    private var bounds: Rect? = null
    private var boundsF: RectF? = null

    private var desaturateColorFilter: ColorMatrixColorFilter? = null

    private var cacheValid = false
    private var cacheBitmap: Bitmap? = null
    private var cachedWidth: Int = 0
    private var cachedHeight: Int = 0

    private val radius = dpToPx(8f)
    private val punchRadius = dpToPx(10f)
    private val countRadius = dpToPx(16f)


    var card: PokemonCard? = null
        set(value) {
            field = value
            loadImage()
        }


    var evolution = Evolution.NONE
        set(value) {
            field = value
            invalidate()
        }

    var count: Int = 1
        set(value) {
            field = value
            invalidate()
        }

    var desaturateOnPress = false
        set(value) {
            field = value
            invalidate()
        }

    var displayCountWhenOne = false
        set(value) {
            field = value
            invalidate()
        }


    init {

        // Other initialization
        blackPaint.color = -0x1000000
        punchPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        maskedPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        countPaint.color = color(R.color.primaryColor)
        countPaint.setShadowLayer(dpToPx(4f), 0f, -1f, color(R.color.black54))
        countTextPaint.textSize = spToPx(12f)
        countTextPaint.color = color(R.color.white)

        // Always want a cache allocated.
        cacheBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        if (desaturateOnPress) {
            // Create a desaturate color filter for pressed state.
            val cm = ColorMatrix()
            cm.setSaturation(0f)
            desaturateColorFilter = ColorMatrixColorFilter(cm)
        }

        setImageResource(R.drawable.pokemon_card_back)
        elevation = dpToPx(4f)
        outlineProvider = CardOutlineProvider(radius)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = Math.round(width * RATIO)
        setMeasuredDimension(width, height)
    }


    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        val changed = super.setFrame(l, t, r, b)
        bounds = Rect(0, 0, r - l, b - t)
        boundsF = RectF(bounds)

        if (changed) {
            cacheValid = false
        }

        return changed
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        if (bounds == null) {
            return
        }

        val width = bounds!!.width()
        val height = bounds!!.height()

        if (width == 0 || height == 0) {
            return
        }

        if (!cacheValid || width != cachedWidth || height != cachedHeight) {
            // Need to redraw the cache
            if (width == cachedWidth && height == cachedHeight) {
                // Have a correct-sized bitmap cache already allocated. Just erase it.
                cacheBitmap!!.eraseColor(0)
            } else {
                // Allocate a new bitmap with the correct dimensions.
                cacheBitmap!!.recycle()

                cacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                cachedWidth = width
                cachedHeight = height
            }

            val cacheCanvas = Canvas(cacheBitmap!!)
            val sc = cacheCanvas.save()

            // Draw masks
            cacheCanvas.drawRoundRect(boundsF, radius, radius, blackPaint)
            drawEvolutionNotches(cacheCanvas)

            maskedPaint.colorFilter = if (desaturateOnPress && isPressed)
                desaturateColorFilter
            else
                null
            cacheCanvas.saveLayer(boundsF, maskedPaint)
            super.onDraw(cacheCanvas)
            cacheCanvas.restoreToCount(sc)

            drawCount(cacheCanvas)
        }

        // Draw from cache
        canvas.drawBitmap(cacheBitmap!!, bounds!!.left.toFloat(), bounds!!.top.toFloat(), null)
    }


    override fun drawableStateChanged() {
        super.drawableStateChanged()
        if (isDuplicateParentStateEnabled) {
            postInvalidateOnAnimation()
        }
    }


    private fun loadImage() {
        GlideApp.with(this)
                .load(card?.imageUrl)
                .placeholder(R.drawable.pokemon_card_back)
                .transition(withCrossFade())
                .into(this)
    }


    private fun drawEvolutionNotches(canvas: Canvas) {
        val y = boundsF?.centerY() ?: 0f / 2f
        when(evolution) {
            Evolution.START -> {
                val x = 0f
                canvas.drawCircle(x, y, punchRadius, punchPaint)
            }
            Evolution.MIDDLE -> {
                val x1 = 0f
                val x2 = boundsF?.right ?: 0f
                canvas.drawCircle(x1, y, punchRadius, punchPaint)
                canvas.drawCircle(x2, y, punchRadius, punchPaint)
            }
            Evolution.END -> {
                val x = boundsF?.right ?: 0f
                canvas.drawCircle(x, y, punchRadius, punchPaint)
            }
        }
    }


    private fun drawCount(canvas: Canvas) {
        if (count > 1 || (displayCountWhenOne && count > 0)) {
            // Draw background
            val x = boundsF?.centerX() ?: 0f
            val y = boundsF?.bottom ?: 0f
            canvas.drawCircle(x, y, countRadius, countPaint)

            // Draw Text
            val text = StaticLayout(count.toString(), countTextPaint, (countRadius * 2).toInt(),
                    Layout.Alignment.ALIGN_CENTER, 1f, 0f, false)

            canvas.save()
            canvas.translate(x - countRadius, y - countTextPaint.textSize - dpToPx(2f))
            text.draw(canvas)
            canvas.restore()
        }
    }


    enum class Evolution {
        START,
        MIDDLE,
        END,
        NONE
    }


    class CardOutlineProvider(private val radius: Float) : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, radius)
        }
    }


    companion object {
        private const val RATIO = 1.3959183673f
    }
}