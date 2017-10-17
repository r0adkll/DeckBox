package com.r0adkll.deckbuilder.arch.ui.widgets


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.*
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.PokemonCard


class PokemonCardView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : ImageView(context, attrs, defStyle) {

    private val blackPaint: Paint = Paint()
    private val maskedPaint: Paint = Paint()
    private val punchPaint: Paint = Paint()

    private var bounds: Rect? = null
    private var boundsF: RectF? = null

    private var desaturateColorFilter: ColorMatrixColorFilter? = null

    private var cacheValid = false
    private var cacheBitmap: Bitmap? = null
    private var cachedWidth: Int = 0
    private var cachedHeight: Int = 0

    private val radius = dpToPx(8f)
    private val punchRadius = dpToPx(10f)

    var evolution = Evolution.NONE
        set(value) {
            field = value
            invalidate()
        }

    var desaturateOnPress = false
        set(value) {
            field = value
            invalidate()
        }


    init {

        // Other initialization
        blackPaint.color = -0x1000000
        punchPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        maskedPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

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


    fun setCard(card: PokemonCard) {
        GlideApp.with(this)
                .load(card.imageUrl)
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