package com.r0adkll.deckbuilder.arch.ui.components.palette

import android.widget.ImageView
import androidx.palette.graphics.Palette
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.ui.components.palette.actions.DefaultExpansionAction
import com.r0adkll.deckbuilder.arch.ui.components.palette.actions.DragonMajestyExpansionAction
import com.r0adkll.deckbuilder.arch.ui.components.palette.actions.ExpansionAction
import com.r0adkll.deckbuilder.arch.ui.components.palette.actions.LostThunderExpansionAction
import com.r0adkll.deckbuilder.arch.ui.components.palette.actions.TeamUpExpansionAction
import com.r0adkll.deckbuilder.util.glide.palette.PaletteBitmapViewTarget

class ExpansionPaletteAction(
    private val view: ImageView,
    private val expansion: Expansion,
    private val actions: List<ExpansionAction> = defaultExpansionActions,
    private val colorPicker: (Palette) -> Int? = defaultColorPicker,
    private val contrastListener: (isLight: Boolean) -> Unit
) : PaletteBitmapViewTarget.PaletteAction {

    override fun execute(palette: Palette?) {
        palette?.let { p ->
            colorPicker(p)?.let { color ->
                val action = actions.find { it.isForExpansion(expansion.code) }
                if (action != null) {
                    action.applyColor(view, color)
                    contrastListener(action.isLight(color))
                }
            }
        }
    }

    companion object {

        private val defaultExpansionActions get() = listOf(
            DragonMajestyExpansionAction,
            LostThunderExpansionAction,
            TeamUpExpansionAction("sm9", "sm10", "sm11", "sm115", "sma", "sm12", "det1"),
            DefaultExpansionAction
        )

        private val defaultColorPicker: (Palette) -> Int? = {
            it.vibrantSwatch?.rgb
        }
    }
}
