package com.r0adkll.deckbuilder.arch.domain.features.editing.model


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


@PaperParcel
data class Session(
        val id: Long,
        val deckId: String?,
        val name: String,
        val description: String,
        val image: DeckImage?,
        val cards: List<PokemonCard>,
        val hasChanges: Boolean,
        val changes: List<Change>
) : PaperParcelable {


    override fun toString(): String {
        return "Session(id=$id, deckId=$deckId, name='$name', description='$description', " +
                "cards=${cards.size}, hasChanges=$hasChanges, changes=${changes.size})"
    }


    companion object {
        @JvmField val CREATOR = PaperParcelSession.CREATOR
    }
}