package com.r0adkll.deckbuilder.arch.data.features.marketplace.api.model

data class PriceResponse(
    val cardId: String,
    val setCode: String,
    val productId: Long,
    val productName: String,
    val groupId: Long,
    val url: String,
    val modifiedOn: String,
    val prices: List<CardPricing>? = null
) 
