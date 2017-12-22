package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.line

import android.view.View
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView


interface EvolutionLineAdapter {

    fun getEvolutionState(view: View): PokemonCardView.Evolution

}