package com.r0adkll.deckbuilder.util.extensions


import android.view.View
import android.widget.EditText


fun View.isVisible(): Boolean = this.visibility == View.VISIBLE

fun EditText.moveCursorToEnd() {
    this.setSelection(this.text.length)
}