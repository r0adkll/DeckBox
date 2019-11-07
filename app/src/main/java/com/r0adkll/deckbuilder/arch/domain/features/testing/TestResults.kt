package com.r0adkll.deckbuilder.arch.domain.features.testing

import android.os.Parcelable
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TestResults(
    val count: Int,
    val mulligans: Int,
    val startingHand: Map<PokemonCard, Int>
) : Parcelable
