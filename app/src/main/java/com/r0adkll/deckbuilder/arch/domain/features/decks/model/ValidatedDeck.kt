package com.r0adkll.deckbuilder.arch.domain.features.decks.model

import android.os.Parcelable
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Validation
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ValidatedDeck(
    val deck: Deck,
    val validation: Validation?
) : Parcelable
