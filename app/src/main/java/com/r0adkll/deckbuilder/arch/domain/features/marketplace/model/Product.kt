package com.r0adkll.deckbuilder.arch.domain.features.marketplace.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val cardId: String,
    val setCode: String,
    val groupId: Long,
    val productId: Long,
    val productName: String,
    val url: String,
    val prices: List<Price>,
    val rarities: Set<String>,
    val modifiedOn: Long
) : Parcelable {

    val topRarity: String?
        get() = (rarities.find { it == "Normal" }
            ?: rarities.find { it == "Holofoil" }
            ?: rarities.find { it == "Reverse Holofoil" }
            ?: rarities.find { it == "1st Edition Normal" }
            ?: rarities.find { it == "1st Edition Holofoil" })

    val latestPrice: Price?
        get() = prices.filter { it.rarity == topRarity }.maxBy { it.updatedAt }

    val marketPrice: Double?
        get() = latestPrice?.market
}
