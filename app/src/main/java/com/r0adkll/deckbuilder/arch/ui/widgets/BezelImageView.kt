/*
 * Copyright (c) 2018 52inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.r0adkll.deckbuilder.arch.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView

import com.ftinc.kit.R


/**
 * An [android.widget.ImageView] that draws its contents inside a mask and draws a border
 * drawable on top. This is useful for applying a beveled look to image contents, but is also
 * flexible enough for use with other desired aesthetics.
 */
open class BezelImageView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : ImageView(context, attrs, defStyle) {

    private val blackPaint: Paint
    private val maskedPaint: Paint

    private var bounds: Rect? = null
    private var boundsF: RectF? = null

    var borderDrawable: Drawable? = null
        set(value) {
            field = value
            field?.callback = this
        }

    var maskDrawable: Drawable? = null
        set(value) {
            field = value
            field?.callback = this
        }

    private var desaturateColorFilter: ColorMatrixColorFilter? = null
    private var desaturateOnPress = false

    private var cacheValid = false
    private var cacheBitmap: Bitmap? = null
    private var cachedWidth: Int = 0
    private var cachedHeight: Int = 0

    init {

        // Attribute initialization
        val a = context.obtainStyledAttributes(attrs, R.styleable.BezelImageView,defStyle, 0)

        maskDrawable = a.getDrawable(R.styleable.BezelImageView_maskDrawable)
        borderDrawable = a.getDrawable(R.styleable.BezelImageView_borderDrawable)
        desaturateOnPress = a.getBoolean(R.styleable.BezelImageView_desaturateOnPress,
                desaturateOnPress)

        a.recycle()

        // Other initialization
        blackPaint = Paint()
        blackPaint.color = Color.BLACK

        maskedPaint = Paint()
        maskedPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        // Always want a cache allocated.
        cacheBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        if (desaturateOnPress) {
            // Create a desaturate color filter for pressed state.
            val cm = ColorMatrix()
            cm.setSaturation(0f)
            desaturateColorFilter = ColorMatrixColorFilter(cm)
        }
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        val changed = super.setFrame(l, t, r, b)
        bounds = Rect(0, 0, r - l, b - t)
        boundsF = RectF(bounds)

        borderDrawable?.bounds = bounds
        maskDrawable?.bounds = bounds

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
            if (maskDrawable != null) {
                val sc = cacheCanvas.save()
                maskDrawable?.draw(cacheCanvas)
                maskedPaint.colorFilter = if (desaturateOnPress && isPressed)
                    desaturateColorFilter
                else
                    null
                cacheCanvas.saveLayer(boundsF, maskedPaint, Canvas.ALL_SAVE_FLAG)
                super.onDraw(cacheCanvas)
                cacheCanvas.restoreToCount(sc)
            } else if (desaturateOnPress && isPressed) {
                val sc = cacheCanvas.save()
                cacheCanvas.drawRect(0f, 0f, cachedWidth.toFloat(), cachedHeight.toFloat(), blackPaint)
                maskedPaint.colorFilter = desaturateColorFilter
                cacheCanvas.saveLayer(boundsF, maskedPaint, Canvas.ALL_SAVE_FLAG)
                super.onDraw(cacheCanvas)
                cacheCanvas.restoreToCount(sc)
            } else {
                super.onDraw(cacheCanvas)
            }

            borderDrawable?.draw(cacheCanvas)
        }

        // Draw from cache
        canvas.drawBitmap(cacheBitmap!!, bounds!!.left.toFloat(), bounds!!.top.toFloat(), null)
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        if (borderDrawable?.isStateful == true) {
            borderDrawable?.state = drawableState
        }
        if (maskDrawable?.isStateful == true) {
            maskDrawable?.state = drawableState
        }
        if (isDuplicateParentStateEnabled) {
            postInvalidateOnAnimation()
        }
    }

    override fun invalidateDrawable(who: Drawable) {
        if (who === borderDrawable || who === maskDrawable) {
            invalidate()
        } else {
            super.invalidateDrawable(who)
        }
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return who === borderDrawable || who === maskDrawable || super.verifyDrawable(who)
    }
}
