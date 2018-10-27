package com.r0adkll.deckbuilder.arch.domain.features.decks.model


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import io.pokemontcg.model.SuperType
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


@PaperParcel
data class Deck(
        val id: String,
        val name: String,
        val description: String,
        val image: DeckImage?,
        val cards: List<PokemonCard>,
        val isMissingCards: Boolean,
        val timestamp: Long
) : PaperParcelable {

    val pokemonCount: Int get() = cards.count { it.supertype == SuperType.POKEMON }
    val trainerCount: Int get() = cards.count { it.supertype == SuperType.TRAINER }
    val energyCount: Int get() = cards.count { it.supertype == SuperType.ENERGY }

    override fun toString(): String {
        return "Deck(id='$id', name='$name', description='$description', cards=${cards.size}, isMissingCards=$isMissingCards, timestamp=$timestamp)"
    }

    companion object {
        @JvmField val CREATOR = PaperParcelDeck.CREATOR
    }
}