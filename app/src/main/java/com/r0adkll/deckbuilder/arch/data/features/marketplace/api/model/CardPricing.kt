package com.r0adkll.deckbuilder.arch.data.features.marketplace.api.model

import kotlinx.serialization.Serializable

@Serializable
data class CardPricing(
    val subTypeName: String,
    val lowPrice: Double?,
    val midPrice: Double?,
    val highPrice: Double?,
    val marketPrice: Double,
    val directLowPrice: Double?,
    val updatedAt: String
) 
