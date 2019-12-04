package com.r0adkll.deckbuilder.arch.data.features.marketplace.api.model

data class ApiListPriceResponse(
    val nextScheduledTime: String,
    val prices: List<PriceResponse>
)
