package com.r0adkll.deckbuilder.arch.ui.components.palette.actions

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.widget.ImageView
import com.r0adkll.deckbuilder.util.extensions.isLight

object DefaultExpansionAction : ExpansionAction {

    override fun isForExpansion(code: String): Boolean {
        return true
    }

    override fun applyColor(view: ImageView, color: Int) {
        view.imageTintList = ColorStateList.valueOf(color)
        view.imageTintMode = PorterDuff.Mode.ADD
    }

    override fun isLight(color: Int): Boolean {
        return color.isLight
    }
}
