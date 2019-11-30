package com.r0adkll.deckbuilder.arch.data.database.relations

import androidx.room.Embedded
import com.r0adkll.deckbuilder.arch.data.database.entities.PriceEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.ProductEntity

data class PriceWithProduct(
    @Embedded val product: ProductEntity,
    @Embedded val price: PriceEntity
)
