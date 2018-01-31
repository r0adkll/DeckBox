package com.r0adkll.deckbuilder.util.extensions


import android.support.annotation.DrawableRes
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.r0adkll.deckbuilder.R


fun View.isVisible(): Boolean = this.visibility == View.VISIBLE

fun EditText.moveCursorToEnd() {
    this.setSelection(this.text.length)
}

/*
 * TextView
 */

fun TextView.drawableStart(@DrawableRes resId: Int) {
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(resId, 0, 0, 0)
}