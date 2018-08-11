package com.r0adkll.deckbuilder.arch.domain.features.testing


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


@PaperParcel
data class TestResults(
        val count: Int, // The total number of shuffle iterations ran
        val mulligans: Int, // The total number of mulligans that occured
        val startingHand: Map<PokemonCard, Int> // The number of times a card has appeared in the starting hand for [count] iterations
) : PaperParcelable {

    companion object {
        @JvmField val CREATOR = PaperParcelTestResults.CREATOR
    }
}