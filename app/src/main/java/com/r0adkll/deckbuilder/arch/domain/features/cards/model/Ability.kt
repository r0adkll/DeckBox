package com.r0adkll.deckbuilder.arch.domain.features.cards.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ability(
    val name: String,
    val text: String
) : Parcelable
