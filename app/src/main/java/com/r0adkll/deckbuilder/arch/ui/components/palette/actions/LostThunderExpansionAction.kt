package com.r0adkll.deckbuilder.arch.ui.components.palette.actions

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Build
import android.widget.ImageView
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.util.extensions.isLight

/**
 * Expansion action for Lost Thunder (sm8)
 */
object LostThunderExpansionAction : ExpansionAction {

    override fun isForExpansion(code: String): Boolean {
        return code == "sm8"
    }

    override fun applyColor(view: ImageView, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setImageResource(R.drawable.dr_smlt_header)
        } else {
            view.imageTintList = ColorStateList.valueOf(color)
            view.imageTintMode = PorterDuff.Mode.ADD
        }
    }

    override fun isLight(color: Int): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            false
        } else {
            color.isLight
        }
    }
}
