package com.r0adkll.deckbuilder.arch.domain.features.cards.model


import android.os.Parcelable
import io.pokemontcg.model.Type
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Attack(
        val cost: List<Type>?,
        val name: String,
        val text: String?,
        val damage: String?,
        val convertedEnergyCost: Int
) : Parcelable