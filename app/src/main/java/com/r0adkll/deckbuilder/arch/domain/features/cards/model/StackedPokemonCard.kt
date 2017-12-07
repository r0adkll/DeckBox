package com.r0adkll.deckbuilder.arch.domain.features.cards.model


data class StackedPokemonCard(
        val card: PokemonCard,
        val count: Int
) {

    override fun equals(other: Any?): Boolean {
        if (this !== other) {
            if (other is StackedPokemonCard) {
                val var2 = other as StackedPokemonCard?
                return this.card == var2!!.card
            }
            return false
        } else {
            return true
        }
    }

}