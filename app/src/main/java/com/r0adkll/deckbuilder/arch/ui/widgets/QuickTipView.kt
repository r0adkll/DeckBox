package com.r0adkll.deckbuilder.arch.ui.widgets

import android.animation.Animator
import android.content.Context
import android.graphics.*
import android.support.annotation.StringRes
import android.support.graphics.drawable.AnimationUtilsCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import com.ftinc.kit.kotlin.extensions.*
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.util.ScreenUtils
import timber.log.Timber


class QuickTipView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val title: TextView

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

        title = TextView(context)
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        title.setTextColor(color(R.color.white))
        title.typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        title.text = "Add a new deck to begin\nbuilding."

        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.END or Gravity.BOTTOM
        lp.marginEnd = dipToPx(16f)
        lp.bottomMargin = dipToPx(152f)

        addView(title, lp)

        paint.color = color(R.color.primaryColor)
        paint.style = Paint.Style.FILL

        eraser.color = Color.BLACK
        eraser.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val d = Math.sqrt(Math.pow((centerX - ev.x).toDouble(), 2.0) +
                    Math.pow((centerY - ev.y).toDouble(), 2.0))
            Timber.i("onInterceptTouchEvent(distance=$d) > $holeRadius")
            return d > holeRadius
        }
        return false
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean = true


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != oldw || h != oldh) {
            val radius = getRadius().toInt() * 2
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
            canvasCache?.drawCircle(offsetX, offsetY, radius, paint)
            canvasCache?.drawCircle(offsetX, offsetY, holeRadius, eraser)

            // render shade at center
            canvas?.drawBitmap(bitmapCache, centerX - offsetX, centerY - offsetY - dipToPx(52f), null)
        }

//        super.onDraw(canvas)
    }


    fun setTitle(@StringRes resId: Int) {
        title.setText(resId)
    }


    fun setTitle(text: CharSequence) {
        title.text = text
    }


    fun show(view: View) {
        val rect = Rect()
        view.getGlobalVisibleRect(rect)
        centerX = rect.exactCenterX()
        centerY = rect.exactCenterY() - (rect.height() / 2f)
        holeRadius = (Math.max(rect.width(), rect.height()).toFloat() / 2f) + dipToPx(16f)
        invalidate()

        val anim = ViewAnimationUtils.createCircularReveal(this, centerX.toInt(), centerY.toInt(), 0f, getRadius())
        this.visible()
        anim.start()
    }


    fun hide(view: View) {
        val rect = Rect()
        view.getGlobalVisibleRect(rect)
        centerX = rect.exactCenterX()
        centerY = rect.exactCenterY() - (rect.height() / 2f)
        holeRadius = (Math.max(rect.width(), rect.height()).toFloat() / 2f) + dipToPx(16f)

        val anim = ViewAnimationUtils.createCircularReveal(this, centerX.toInt(), centerY.toInt(), getRadius(), 0f)
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                this@QuickTipView.gone()
            }
        })
        anim.start()
    }


    private fun getRadius(): Float {
        return if (ScreenUtils.smallestWidth(resources, ScreenUtils.Config.TABLET_10)) {
            dpToPx(300f)
        } else {
            Math.max(measuredWidth / 2f, measuredHeight / 2f) + dpToPx(56f)
        }
    }
}