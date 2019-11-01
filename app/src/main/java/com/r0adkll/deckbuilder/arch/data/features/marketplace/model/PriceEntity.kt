package com.r0adkll.deckbuilder.arch.data.features.marketplace.model

class PriceEntity(
    val productId: Int = 0,
    val subTypeName: String = "",
    val marketPrice: Double = 0.0,
    val highPrice: Double? = null,
    val midPrice: Double? = null,
    val lowPrice: Double? = null,
    val directLowPrice: Double? = null
)
