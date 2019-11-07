package com.r0adkll.deckbuilder.arch.ui.components.palette.actions

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.widget.ImageView
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.util.extensions.isLight

class TeamUpExpansionAction(
    private vararg val codes: String
) : ExpansionAction {

    override fun isForExpansion(code: String): Boolean {
        return codes.contains(code)
    }

    override fun applyColor(view: ImageView, color: Int) {
        view.setImageResource(R.drawable.dr_smtu_background)
        view.imageTintList = ColorStateList.valueOf(color)
        view.imageTintMode = PorterDuff.Mode.ADD
    }

    override fun isLight(color: Int): Boolean {
        return color.isLight
    }
}
