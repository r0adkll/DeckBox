package com.r0adkll.deckbuilder.arch.domain.features.cards.model

import android.os.Parcelable
import io.pokemontcg.model.Type
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Effect(
    val type: Type,
    val value: String
) : Parcelable
