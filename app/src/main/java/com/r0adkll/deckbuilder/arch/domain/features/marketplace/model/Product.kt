package com.r0adkll.deckbuilder.arch.domain.features.marketplace.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
        val cardId: String,
        val setCode: String,
        val groupId: Int,
        val productId: Int,
        val productName: String,
        val url: String,
        val prices: List<Price>,
        val recordedAt: Long
) : Parcelable {

    val price: Price?
        get() = (prices.find { it.rarity == "Normal" }
                ?: prices.find { it.rarity == "Holofoil" }
                ?: prices.find { it.rarity == "Reverse Holofoil" }
                ?: prices.find { it.rarity == "1st Edition Normal" }
                ?: prices.find { it.rarity == "1st Edition Holofoil" })

    val marketPrice: Double?
        get() = price?.market
}
