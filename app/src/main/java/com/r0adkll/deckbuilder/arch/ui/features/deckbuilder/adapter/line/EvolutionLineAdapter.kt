package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.line

import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView

interface EvolutionLineAdapter {

    fun getEvolutionState(position: Int): State

    data class State(
        val nodeIndex: Int,
        val cardIndex: Int,
        val evolution: PokemonCardView.Evolution
    )
}
