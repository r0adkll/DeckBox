package com.r0adkll.deckbuilder.arch.ui.widgets

import android.graphics.drawable.Drawable


/**
 * Special drawable wrapper that returns -1 for minimum width/height, and intrinsic width/height
 */
class BackgroundDrawableWrapper(drawable: Drawable) : DrawableWrapper(drawable) {

    override fun getMinimumWidth(): Int = -1
    override fun getMinimumHeight(): Int = -1
    override fun getIntrinsicWidth(): Int = -1
    override fun getIntrinsicHeight(): Int = -1
}