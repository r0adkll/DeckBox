package com.r0adkll.deckbuilder.arch.domain.features.editing.model


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


@PaperParcel
data class Session(
        val id: Long,
        val deckId: String?,
        val name: String,
        val description: String,
        val cards: List<PokemonCard>,
        val hasChanges: Boolean,
        val changes: List<Change>
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelSession.CREATOR
    }
}