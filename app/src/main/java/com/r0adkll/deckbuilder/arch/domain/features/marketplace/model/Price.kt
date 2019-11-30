package com.r0adkll.deckbuilder.arch.domain.features.marketplace.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Price(
    val rarity: String,
    val low: Double?,
    val mid: Double?,
    val high: Double?,
    val market: Double?,
    val directLow: Double?,
    val updatedAt: Long,
    val expiresAt: Long
) : Parcelable
