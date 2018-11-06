package com.r0adkll.deckbuilder.arch.domain.features.testing


import android.os.Parcelable
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import kotlinx.android.parcel.Parcelize


@Parcelize
data class TestResults(
        val count: Int, // The total number of shuffle iterations ran
        val mulligans: Int, // The total number of mulligans that occured
        val startingHand: Map<PokemonCard, Int> // The number of times a card has appeared in the starting hand for [count] iterations
) : Parcelable