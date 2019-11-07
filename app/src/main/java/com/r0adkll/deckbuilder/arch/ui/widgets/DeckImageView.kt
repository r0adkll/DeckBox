@file:Suppress("MagicNumber", "ComplexMethod")

package com.r0adkll.deckbuilder.arch.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.ColorInt
import com.ftinc.kit.extensions.color
import com.ftinc.kit.extensions.dip
import com.ftinc.kit.extensions.dp
import com.ftinc.kit.extensions.drawable
import com.r0adkll.deckbuilder.R
import io.pokemontcg.model.Type

class DeckImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageScaleView(context, attrs, defStyleAttr) {

    private val mBlackPaint: Paint
    private val mMaskedPaint: Paint
    private val mTypePaint: Paint

    private var mBounds: Rect? = null
    private var mBoundsF: RectF? = null

    private val mBorderDrawable: Drawable?
    private val mMaskDrawable: Drawable?

    private var mDesaturateColorFilter: ColorMatrixColorFilter? = null
    private var mDesaturateOnPress = false

    private var mCacheValid = false
    private var mCacheBitmap: Bitmap? = null
    private var mCachedWidth: Int = 0
    private var mCachedHeight: Int = 0

    private val primaryTypeRect = Rect()
    private val secondaryTypeRect = Rect()

    var forceAspectRatio = false
    var primaryType: Type? = null
    var secondaryType: Type? = null

    var topCropEnabled: Boolean = false
        set(value) {
            field = value
            matrixType = MatrixCropType.TOP_CENTER
            scaleType = ScaleType.MATRIX
        }

    init {
        // Attribute initialization
        val a = context.obtainStyledAttributes(attrs, R.styleable.DeckImageView,
            defStyleAttr, 0)

        mMaskDrawable = a.getDrawable(R.styleable.DeckImageView_maskDrawable)
        if (mMaskDrawable != null) {
            mMaskDrawable.callback = this
        }

        mBorderDrawable = a.getDrawable(R.styleable.DeckImageView_borderDrawable)
        if (mBorderDrawable != null) {
            mBorderDrawable.callback = this
        }

        mDesaturateOnPress = a.getBoolean(R.styleable.DeckImageView_desaturateOnPress,
            mDesaturateOnPress)

        val pokeType = a.getInteger(R.styleable.DeckImageView_primaryType, -1)
        primaryType = when (pokeType) {
            0 -> Type.COLORLESS
            1 -> Type.FIRE
            2 -> Type.GRASS
            3 -> Type.WATER
            4 -> Type.LIGHTNING
            5 -> Type.FIGHTING
            6 -> Type.PSYCHIC
            7 -> Type.METAL
            8 -> Type.DRAGON
            9 -> Type.FAIRY
            10 -> Type.DARKNESS
            else -> null
        }

        val pokeType2 = a.getInteger(R.styleable.DeckImageView_secondaryType, -1)
        secondaryType = when (pokeType2) {
            0 -> Type.COLORLESS
            1 -> Type.FIRE
            2 -> Type.GRASS
            3 -> Type.WATER
            4 -> Type.LIGHTNING
            5 -> Type.FIGHTING
            6 -> Type.PSYCHIC
            7 -> Type.METAL
            8 -> Type.DRAGON
            9 -> Type.FAIRY
            10 -> Type.DARKNESS
            else -> null
        }

        forceAspectRatio = a.getBoolean(R.styleable.DeckImageView_forceAspectRatio, false)

        a.recycle()

        // Other initialization
        mBlackPaint = Paint()
        mBlackPaint.color = -0x1000000

        mMaskedPaint = Paint()
        mMaskedPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        mTypePaint = Paint()

        // Always want a cache allocated.
        mCacheBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        if (mDesaturateOnPress) {
            // Create a desaturate color filter for pressed state.
            val cm = ColorMatrix()
            cm.setSaturation(0f)
            mDesaturateColorFilter = ColorMatrixColorFilter(cm)
        }

        outlineProvider = CardOutlineProvider(dp(4))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (forceAspectRatio) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = Math.round(width * PokemonCardView.RATIO)
            setMeasuredDimension(width, height)
        }
    }

    override fun setFrame(frameLeft: Int, frameTop: Int, frameRight: Int, frameBottom: Int): Boolean {
        val changed = super.setFrame(frameLeft, frameTop, frameRight, frameBottom)
        mBounds = Rect(0, 0, frameRight - frameLeft, frameBottom - frameTop)
        mBoundsF = RectF(mBounds)

        if (mBorderDrawable != null) {
            mBorderDrawable.bounds = mBounds!!
        }
        if (mMaskDrawable != null) {
            mMaskDrawable.bounds = mBounds!!
        }

        if (changed) {
            mCacheValid = false
        }

        return changed
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        if (mBounds == null) {
            return
        }

        val width = mBounds?.width() ?: 0
        val height = mBounds?.height() ?: 0

        if (width == 0 || height == 0) {
            return
        }

        if (!mCacheValid || width != mCachedWidth || height != mCachedHeight) {
            // Need to redraw the cache
            if (width == mCachedWidth && height == mCachedHeight) {
                // Have a correct-sized bitmap cache already allocated. Just erase it.
                mCacheBitmap?.eraseColor(0)
            } else {
                // Allocate a new bitmap with the correct dimensions.
                mCacheBitmap?.recycle()

                mCacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                mCachedWidth = width
                mCachedHeight = height
            }

            val cacheCanvas = Canvas(mCacheBitmap!!)
            if (mMaskDrawable != null) {
                val sc = cacheCanvas.save()
                mMaskDrawable.draw(cacheCanvas)
                mMaskedPaint.colorFilter = if (mDesaturateOnPress && isPressed)
                    mDesaturateColorFilter
                else
                    null
                cacheCanvas.saveLayer(mBoundsF, mMaskedPaint)
                if (!drawTypes(cacheCanvas)) {
                    super.onDraw(cacheCanvas)
                }
                cacheCanvas.restoreToCount(sc)
            } else if (mDesaturateOnPress && isPressed) {
                val sc = cacheCanvas.save()
                cacheCanvas.drawRect(0f, 0f, mCachedWidth.toFloat(), mCachedHeight.toFloat(), mBlackPaint)
                mMaskedPaint.colorFilter = mDesaturateColorFilter
                cacheCanvas.saveLayer(mBoundsF, mMaskedPaint)
                if (!drawTypes(cacheCanvas)) {
                    super.onDraw(cacheCanvas)
                }
                cacheCanvas.restoreToCount(sc)
            } else {
                if (!drawTypes(cacheCanvas)) {
                    super.onDraw(cacheCanvas)
                }
            }

            mBorderDrawable?.draw(cacheCanvas)
        }

        // Draw from cache
        canvas.drawBitmap(mCacheBitmap!!,
            mBounds?.left?.toFloat() ?: 0f,
            mBounds?.top?.toFloat() ?: 0f,
            null)
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        if (mBorderDrawable != null && mBorderDrawable.isStateful) {
            mBorderDrawable.state = drawableState
        }
        if (mMaskDrawable != null && mMaskDrawable.isStateful) {
            mMaskDrawable.state = drawableState
        }
        if (isDuplicateParentStateEnabled) {
            postInvalidateOnAnimation()
        }
    }

    override fun invalidateDrawable(who: Drawable) {
        if (who === mBorderDrawable || who === mMaskDrawable) {
            invalidate()
        } else {
            super.invalidateDrawable(who)
        }
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return who === mBorderDrawable || who === mMaskDrawable || super.verifyDrawable(who)
    }

    fun clear() {
        primaryType = null
        secondaryType = null
        setImageDrawable(null)
    }

    private fun drawTypes(canvas: Canvas): Boolean {
        if (primaryType == null) return false

        val width = mBounds?.width() ?: 0
        val height = mBounds?.height() ?: 0

        // Render the first type
        primaryType?.let {
            val typeDrawable = getTypeDrawable(it)
            typeDrawable.setBounds(0, 0, dip(24), dip(24))
            mTypePaint.color = getTypeColor(it)
            primaryTypeRect.set(0, 0, if (secondaryType == null) width else width / 2, height)
            canvas.drawRect(primaryTypeRect, mTypePaint)

            val x = (if (secondaryType == null) width / 2f else (width * .25f)) - (typeDrawable.intrinsicWidth / 2f)
            val y = (height / 2f) - (typeDrawable.intrinsicHeight / 2f)
            canvas.save()
            canvas.translate(x, y)
            typeDrawable.draw(canvas)
            canvas.restore()
        }

        // Render the second type
        secondaryType?.let {
            val typeDrawable2 = getTypeDrawable(it)
            typeDrawable2.setBounds(0, 0, dip(24), dip(24))
            mTypePaint.color = getTypeColor(it)
            secondaryTypeRect.set(width / 2, 0, width, height)
            canvas.drawRect(secondaryTypeRect, mTypePaint)

            val x2 = (width * 0.75f) - (typeDrawable2.intrinsicWidth / 2f)
            val y2 = (height / 2f) - (typeDrawable2.intrinsicHeight / 2f)
            canvas.save()
            canvas.translate(x2, y2)
            typeDrawable2.draw(canvas)
            canvas.restore()
        }

        return true
    }

    @ColorInt
    private fun getTypeColor(type: Type): Int = color(when (type) {
        Type.COLORLESS -> R.color.poketype_colorless
        Type.FIRE -> R.color.poketype_fire
        Type.GRASS -> R.color.poketype_grass
        Type.WATER -> R.color.poketype_water
        Type.LIGHTNING -> R.color.poketype_electric
        Type.FIGHTING -> R.color.poketype_fighting
        Type.PSYCHIC -> R.color.poketype_psychic
        Type.METAL -> R.color.poketype_steel
        Type.DRAGON -> R.color.poketype_dragon
        Type.FAIRY -> R.color.poketype_fairy
        Type.DARKNESS -> R.color.poketype_dark
        else -> R.color.poketype_colorless
    })

    private fun getTypeDrawable(type: Type): Drawable = drawable(when (type) {
        Type.COLORLESS -> R.drawable.ic_poketype_colorless
        Type.FIRE -> R.drawable.ic_poketype_fire
        Type.GRASS -> R.drawable.ic_poketype_grass
        Type.WATER -> R.drawable.ic_poketype_water
        Type.LIGHTNING -> R.drawable.ic_poketype_electric
        Type.FIGHTING -> R.drawable.ic_poketype_fighting
        Type.PSYCHIC -> R.drawable.ic_poketype_psychic
        Type.METAL -> R.drawable.ic_poketype_steel
        Type.DRAGON -> R.drawable.ic_poketype_dragon
        Type.FAIRY -> R.drawable.ic_poketype_fairy
        Type.DARKNESS -> R.drawable.ic_poketype_dark
        else -> R.drawable.ic_poketype_colorless
    })!!

    class CardOutlineProvider(private val radius: Float) : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, radius)
        }
    }
}
