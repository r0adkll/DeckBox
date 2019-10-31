package com.r0adkll.deckbuilder.arch.ui.widgets

import android.content.Context
import android.util.AttributeSet

import com.r0adkll.deckbuilder.R

import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.max


/**
 * Scale to center top or scale to center bottom
 *
 * @author sromku
 */
open class ImageScaleView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle) {

    var matrixType = MatrixCropType.NONE // default

    enum class MatrixCropType(private val mValue: Int) {
        NONE(-1),
        TOP_CENTER(0),
        BOTTOM_CENTER(1);


        companion object {

            fun fromValue(value: Int): MatrixCropType {
                for (matrixCropType in values()) {
                    if (matrixCropType.mValue == value) {
                        return matrixCropType
                    }
                }

                // default
                return NONE
            }
        }
    }

    init {

        // get attributes
        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(attrs, R.styleable.ImageScaleView, 0, 0)
            try {
                matrixType = MatrixCropType.fromValue(a.getInteger(R.styleable.ImageScaleView_matrixType, 0))
            } finally {
                a.recycle()
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        recomputeImgMatrix()
    }

    override fun setFrame(frameLeft: Int, frameTop: Int, frameRight: Int, frameBottom: Int): Boolean {

        val drawable = drawable
        if (drawable != null && matrixType != MatrixCropType.NONE) {

            val frameWidth = (frameRight - frameLeft).toFloat()
            val frameHeight = (frameBottom - frameTop).toFloat()

            val originalImageWidth = getDrawable().intrinsicWidth.toFloat()
            val originalImageHeight = getDrawable().intrinsicHeight.toFloat()

            var usedScaleFactor: Float

//            if (frameWidth > originalImageWidth || frameHeight > originalImageHeight) {
                // If frame is bigger than image
                // => Crop it, keep aspect ratio and position it at the bottom
                // and
                // center horizontally
                val fitHorizontallyScaleFactor = frameWidth / originalImageWidth
                val fitVerticallyScaleFactor = frameHeight / originalImageHeight

                usedScaleFactor = Math.max(fitHorizontallyScaleFactor, fitVerticallyScaleFactor)
//            } else {
//                val fitHorizontallyScaleFactor = originalImageWidth / frameWidth
//
//            }

            val newImageWidth = originalImageWidth * usedScaleFactor
            val newImageHeight = originalImageHeight * usedScaleFactor

            val matrix = imageMatrix
            matrix.setScale(usedScaleFactor, usedScaleFactor, 0f, 0f)

            when (matrixType) {
                MatrixCropType.TOP_CENTER -> matrix.postTranslate((frameWidth - newImageWidth) / 2, 0f)
                MatrixCropType.BOTTOM_CENTER -> matrix.postTranslate((frameWidth - newImageWidth) / 2, frameHeight - newImageHeight)

                else -> {
                }
            }

            imageMatrix = matrix
        }
        return super.setFrame(frameLeft, frameTop, frameRight, frameBottom)
    }

    private fun recomputeImgMatrix() {
        val drawable = drawable
        if (drawable != null && matrixType != MatrixCropType.NONE) {
            val frameWidth = width - paddingLeft - paddingRight
            val frameHeight = height - paddingTop - paddingBottom

            val originalImageWidth = getDrawable().intrinsicWidth.toFloat()
            val originalImageHeight = getDrawable().intrinsicHeight.toFloat()

            var usedScaleFactor: Float

//            if (frameWidth > originalImageWidth || frameHeight > originalImageHeight) {
                // If frame is bigger than image
                // => Crop it, keep aspect ratio and position it at the bottom
                // and
                // center horizontally
                val fitHorizontallyScaleFactor = frameWidth / originalImageWidth
                val fitVerticallyScaleFactor = frameHeight / originalImageHeight

                usedScaleFactor = max(fitHorizontallyScaleFactor, fitVerticallyScaleFactor)
//            }

            val newImageWidth = originalImageWidth * usedScaleFactor
            val newImageHeight = originalImageHeight * usedScaleFactor

            val matrix = imageMatrix
            matrix.setScale(usedScaleFactor, usedScaleFactor, 0f, 0f)

            when (matrixType) {
                MatrixCropType.TOP_CENTER -> matrix.postTranslate((frameWidth - newImageWidth) / 2, 0f)
                MatrixCropType.BOTTOM_CENTER -> matrix.postTranslate((frameWidth - newImageWidth) / 2, frameHeight - newImageHeight)
                else -> {
                }
            }

            imageMatrix = matrix
        }
    }

}
