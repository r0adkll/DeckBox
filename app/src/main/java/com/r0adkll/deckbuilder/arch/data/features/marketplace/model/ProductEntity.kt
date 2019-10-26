package com.r0adkll.deckbuilder.arch.data.features.marketplace.model

import com.google.firebase.Timestamp

class ProductEntity(
    val cardId: String = "",
    val setCode: String = "",
    val productId: Int = 0,
    val productName: String = "",
    val groupId: Int = 0,
    val url: String = "",
    val prices: List<PriceEntity> = emptyList(),
    val updatedAt: Timestamp = Timestamp(0L, 0)
)
