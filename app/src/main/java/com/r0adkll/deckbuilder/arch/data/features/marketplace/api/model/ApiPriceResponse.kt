package com.r0adkll.deckbuilder.arch.data.features.marketplace.api.model

data class ApiPriceResponse(
    val nextScheduledTime: String,
    val price: PriceResponse
)
