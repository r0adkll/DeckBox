package com.r0adkll.deckbuilder.arch.ui.components.palette

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.ui.components.palette.actions.ExpansionAction
import com.r0adkll.deckbuilder.util.glide.palette.PaletteBitmapViewTarget


class ExpansionPaletteAction(
        val expansion: Expansion,
        val actions: List<ExpansionAction>,
        val colorPicker: (Palette) -> Int? = defaultColorPicker
) : PaletteBitmapViewTarget.PaletteAction {

    override fun execute(palette: Palette?) {
        palette?.let { p ->
            colorPicker.invoke(p)?.let { color ->
                val action = actions.find { it.code == expansion.code }
                if (action != null) {
                    val isLight = ColorUtils.calculateContrast(Color.WHITE, color) < 3.0
                    action.applyColor(color, isLight)
                }
            }
        }
    }


    companion object {

        private val defaultColorPicker: (Palette) -> Int? = {
            it.vibrantSwatch?.rgb
        }
    }
}
