package com.r0adkll.deckbuilder.arch.domain.features.marketplace.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PriceRarity(
    val id: Int,
    val key: String,
    val name: String
) : Parcelable
