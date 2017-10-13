package com.r0adkll.deckbuilder.arch.domain


import io.pokemontcg.model.SuperType
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


@PaperParcel
data class Deck(
        val name: String,
        val description: String,
        val cards: List<Card>
) : PaperParcelable {

    val standardLegal: Boolean get() = cards.none { !it.set.standardLegal }
    val expandedLegal: Boolean get() = cards.none { !it.set.expandedLegal }
    val pokemonCount: Int get() = cards.count { it.supertype == SuperType.POKEMON }
    val trainerCount: Int get() = cards.count { it.supertype == SuperType.TRAINER }
    val energyCount: Int get() = cards.count { it.supertype == SuperType.ENERGY }


    companion object {
        @JvmField val CREATOR = PaperParcelDeck.CREATOR
    }
}