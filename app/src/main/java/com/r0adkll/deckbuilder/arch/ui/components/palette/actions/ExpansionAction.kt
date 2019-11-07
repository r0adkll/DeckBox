package com.r0adkll.deckbuilder.arch.ui.components.palette.actions

import android.widget.ImageView
import androidx.annotation.ColorInt

interface ExpansionAction {

    fun isForExpansion(code: String): Boolean
    fun applyColor(view: ImageView, color: Int)
    fun isLight(@ColorInt color: Int): Boolean
}
