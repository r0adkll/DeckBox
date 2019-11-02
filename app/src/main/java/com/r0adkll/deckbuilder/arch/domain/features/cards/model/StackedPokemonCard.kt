package com.r0adkll.deckbuilder.arch.domain.features.cards.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StackedPokemonCard(
    val card: PokemonCard,
    val count: Int = 1,
    val collection: Int? = null
) : Parcelable {

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

    override fun hashCode(): Int {
        var result = card.hashCode()
        result = 31 * result + count.hashCode()
        result = 31 * result + (collection?.hashCode() ?: 0)
        return result
    }
}
