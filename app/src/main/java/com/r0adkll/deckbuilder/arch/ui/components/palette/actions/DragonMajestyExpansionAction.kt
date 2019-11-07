package com.r0adkll.deckbuilder.arch.ui.components.palette.actions

import android.graphics.BitmapFactory
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.widget.ImageView
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.util.extensions.isLight

/**
 * Expansion action for SM7.5, Dragon Majesty
 */
@Suppress("MagicNumber")
object DragonMajestyExpansionAction : ExpansionAction {

    override fun isForExpansion(code: String): Boolean {
        return code == "sm75"
    }

    override fun applyColor(view: ImageView, color: Int) {
        val background = ColorDrawable(color)
        val pattern = BitmapFactory.decodeResource(view.resources, R.drawable.dr_scales_pattern)
        val foreground = BitmapDrawable(view.resources, pattern).apply {
            setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
            setTargetDensity(view.resources.displayMetrics.densityDpi * 4)
        }
        view.setImageDrawable(LayerDrawable(arrayOf(background, foreground)))
    }

    override fun isLight(color: Int): Boolean {
        return color.isLight
    }
}
