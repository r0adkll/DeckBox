package com.r0adkll.deckbuilder.arch.ui.widgets


import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.ftinc.kit.kotlin.extensions.spToPx
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.roundToInt


class PokemonCardView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : ForegroundImageView(context, attrs, defStyle) {

    private val blackPaint: Paint = Paint()
    private val maskedPaint: Paint = Paint()
    private val punchPaint: Paint = Paint()
    private val countPaint: Paint = Paint()
    private val countTextPaint: TextPaint = TextPaint(TextPaint.LINEAR_TEXT_FLAG)

    private val bounds = Rect()
    private val boundsF = RectF()

    private var desaturateColorFilter: ColorMatrixColorFilter? = null

    private var cacheValid = false
    private var cacheBitmap: Bitmap? = null
    private var cachedWidth: Int = 0
    private var cachedHeight: Int = 0

    private val radius = dpToPx(4f)
    private val punchRadius = dpToPx(10f)
    private val countRadius = dpToPx(16f)

    private var lastTouchX: Float = 0f
    private var lastTouchY: Float = 1f

    var card: PokemonCard? = null
        set(value) {
            if (field?.id != value?.id) {
                field = value
                loadImage()
            }
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

    var startDragImmediately = false

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
        val height = (width * RATIO).roundToInt()
        setMeasuredDimension(width, height)
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        val changed = super.setFrame(l, t, r, b)
        bounds.set(0, 0, r - l, b - t)
        boundsF.set(bounds)

        if (changed) {
            cacheValid = false
        }

        return changed
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val width = bounds.width()
        val height = bounds.height()

        if (width == 0 || height == 0) {
            return
        }

        if (!cacheValid || width != cachedWidth || height != cachedHeight) {
            // Need to redraw the cache
            if (width == cachedWidth && height == cachedHeight) {
                // Have a correct-sized bitmap cache already allocated. Just erase it.
                if (cacheBitmap?.isRecycled == false) {
                    cacheBitmap?.eraseColor(0)
                }
            } else {
                // Allocate a new bitmap with the correct dimensions.
                cacheBitmap?.recycle()

                try {
                    cacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    cachedWidth = width
                    cachedHeight = height
                } catch (e: OutOfMemoryError) {
                    Timber.w(e) // FIXME: Critical error report
                    return
                }
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
        canvas.drawBitmap(cacheBitmap!!, bounds.left.toFloat(), bounds.top.toFloat(), null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val dX = event.x - lastTouchX
        val dY = event.y - lastTouchY

        if (startDragImmediately) {
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    if (abs(dX) > abs(dY)) {
                        // Trigger drag in 'new' mode, i.e. we plan to add this card, new, to a deck
                        startDrag(false)
                    }
                }
            }
        }

        lastTouchX = event.x
        lastTouchY = event.y

        return super.onTouchEvent(event)
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        if (isDuplicateParentStateEnabled) {
            postInvalidateOnAnimation()
        }
    }

    @Suppress("DEPRECATION")
    fun startDrag(isEdit: Boolean) {
        val state = DragState(this, isEdit)
        val clipData = ClipData.newPlainText(KEY_CARD, KEY_CARD)
        val shadowBuilder = CardShadowBuilder(this, PointF(lastTouchX, lastTouchY))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            startDragAndDrop(clipData, shadowBuilder, state, 0)
        }
        else {
            startDrag(clipData, shadowBuilder, state, 0)
        }

        imageAlpha = (255f * 54f).toInt()
    }

    private fun loadImage() {
        GlideApp.with(this)
                .load(card?.imageUrl)
                .placeholder(R.drawable.pokemon_card_back)
                .into(this)
    }

    @Suppress("NON_EXHAUSTIVE_WHEN")
    private fun drawEvolutionNotches(canvas: Canvas) {
        val y = boundsF.centerY()
        when(evolution) {
            Evolution.START -> {
                val x = 0f
                canvas.drawCircle(x, y, punchRadius, punchPaint)
            }
            Evolution.MIDDLE -> {
                val x1 = 0f
                val x2 = boundsF.right
                canvas.drawCircle(x1, y, punchRadius, punchPaint)
                canvas.drawCircle(x2, y, punchRadius, punchPaint)
            }
            Evolution.END -> {
                val x = boundsF.right
                canvas.drawCircle(x, y, punchRadius, punchPaint)
            }
        }
    }

    private fun drawCount(canvas: Canvas) {
        if (count > 1 || (displayCountWhenOne && count > 0)) {
            // Draw background
            val x = boundsF.centerX()
            val y = boundsF.bottom
            canvas.drawCircle(x, y, countRadius, countPaint)

            // Draw Text
            val text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                StaticLayout.Builder.obtain(count.toString(), 0, count.toString().length, countTextPaint, (countRadius * 2).toInt())
                        .setAlignment(Layout.Alignment.ALIGN_CENTER)
                        .setLineSpacing(0f, 1f)
                        .setIncludePad(false)
                        .build()
            } else {
                @Suppress("DEPRECATION")
                StaticLayout(count.toString(), countTextPaint, (countRadius * 2).toInt(),
                        Layout.Alignment.ALIGN_CENTER, 1f, 0f, false)
            }

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

    class CardShadowBuilder(view: View, private val lastTouch: PointF) : DragShadowBuilder(view) {

        private var bitmapCache: Bitmap? = null
        private var canvasCache: Canvas? = null
        private val destRect: Rect = Rect()


        override fun onProvideShadowMetrics(outShadowSize: Point, outShadowTouchPoint: Point) {
            view?.let {
                val width = it.width.coerceAtLeast(0) * SHADOW_SIZE_RATIO
                val height = it.height.coerceAtLeast(0) * SHADOW_SIZE_RATIO
                val touchX = lastTouch.x.coerceAtLeast(0f) * SHADOW_SIZE_RATIO
                val touchY = lastTouch.y.coerceAtLeast(0f) * SHADOW_SIZE_RATIO
                outShadowSize.set(width.toInt(), height.toInt())
                outShadowTouchPoint.set(touchX.toInt(), touchY.toInt())
            }
        }

        override fun onDrawShadow(canvas: Canvas) {
            view?.let {
                if (bitmapCache == null) {
                    bitmapCache = Bitmap.createBitmap(it.width, it.height, Bitmap.Config.ARGB_8888)
                    canvasCache = Canvas(bitmapCache!!)
                    it.draw(canvasCache)
                }

                destRect.set(0, 0, canvas.width, canvas.height)
                canvas.drawBitmap(bitmapCache!!, null, destRect, null)
            }
        }


        companion object {
            private const val SHADOW_SIZE_RATIO = 1.25f
        }
    }

    /**
     * Represents the state of a pokemon card drag and drop operation
     */
    data class DragState(
            val view: PokemonCardView,
            val isEdit: Boolean
    )

    companion object {
        private const val KEY_CARD = "PokemonCardView.Card"
        const val RATIO = 1.3959183673f
    }
}
