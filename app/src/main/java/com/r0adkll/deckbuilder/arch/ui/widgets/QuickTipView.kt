package com.r0adkll.deckbuilder.arch.ui.widgets

import android.content.Context
import android.graphics.*
import android.support.graphics.drawable.AnimationUtilsCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.FrameLayout
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.ftinc.kit.kotlin.extensions.invisible
import com.ftinc.kit.kotlin.extensions.visible
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.util.ScreenUtils


class QuickTipView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var centerX: Float = -1f
    private var centerY: Float = -1f
    private var holeRadius: Float = -1f

    private var bitmapCache: Bitmap? = null
    private var canvasCache: Canvas? = null

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val eraser = Paint()


    init {
        this.invisible()
        setWillNotDraw(false)

        paint.color = color(R.color.primaryColor)
        paint.style = Paint.Style.FILL

        eraser.color = Color.BLACK
        eraser.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != oldw || h != oldh) {
            val radius = getRadius().toInt()
            bitmapCache?.recycle()
            bitmapCache = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888)
            canvasCache = Canvas(bitmapCache)
        }
    }


    override fun onDraw(canvas: Canvas?) {

        if (bitmapCache == null) {
            val radius = getRadius().toInt()
            bitmapCache?.recycle()
            bitmapCache = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888)
            canvasCache = Canvas(bitmapCache)
        }

        // Draw our shade
        if (centerX != -1f && centerY != -1f && holeRadius != -1f) {
            val offsetX = (bitmapCache?.width?.toFloat() ?: 0f) / 2f
            val offsetY = (bitmapCache?.height?.toFloat() ?: 0f) / 2f
            val radius = getRadius()
//            canvasCache?.drawColor(color(R.color.primaryColor))
            canvasCache?.drawCircle(offsetX, offsetY, radius, paint)
            canvasCache?.drawCircle(offsetX, offsetY, holeRadius, eraser)

            // render shade at center
            canvas?.drawBitmap(bitmapCache, centerX - offsetX, centerY - offsetY, null)
        }

        //super.onDraw(canvas)
    }


    fun show(view: View) {
        val rect = Rect()
        view.getGlobalVisibleRect(rect)
        centerX = rect.exactCenterX()
        centerY = rect.exactCenterY() - (rect.height() / 2f)
        holeRadius = Math.max(rect.width(), rect.height()).toFloat()
        invalidate()

        val anim = ViewAnimationUtils.createCircularReveal(this, centerX.toInt(), centerY.toInt(), 0f, getRadius())
        this.visible()
        anim.start()
    }


    private fun getRadius(): Float {
        return if (ScreenUtils.smallestWidth(resources, ScreenUtils.Config.TABLET_10)) {
            dpToPx(300f)
        } else {
            Math.max(measuredWidth / 2f, measuredHeight / 2f)
        }
    }
}