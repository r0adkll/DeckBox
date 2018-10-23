package com.r0adkll.deckbuilder.arch.domain.features.cards.model

import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class StackedPokemonCard(
        val card: PokemonCard,
        val count: Int
): PaperParcelable {

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

    companion object {
        @JvmField val CREATOR = PaperParcelStackedPokemonCard.CREATOR
    }
}