package com.r0adkll.deckbuilder.arch.data.features.marketplace.api.model

data class CardPricing(
    val subTypeName: String,
    val lowPrice: Double? = null,
    val midPrice: Double? = null,
    val highPrice: Double? = null,
    val marketPrice: Double,
    val directLowPrice: Double? = null,
    val updatedAt: String
)
