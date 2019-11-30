package com.r0adkll.deckbuilder.arch.data.features.marketplace.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiPriceResponse(
    val nextScheduledTime: String,
    val price: PriceResponse
)



