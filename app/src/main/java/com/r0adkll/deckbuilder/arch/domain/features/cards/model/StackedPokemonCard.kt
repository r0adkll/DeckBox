package com.r0adkll.deckbuilder.arch.domain.features.cards.model


data class StackedPokemonCard(
        val card: PokemonCard,
        val count: Int
) {

    override fun equals(other: Any?): Boolean {
        return card == other
    }
}