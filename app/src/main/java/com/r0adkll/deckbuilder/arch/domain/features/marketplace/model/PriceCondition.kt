package com.r0adkll.deckbuilder.arch.domain.features.marketplace.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PriceCondition(
        val id: Int,
        val name: String,
        val abbreviation: String
): Parcelable
