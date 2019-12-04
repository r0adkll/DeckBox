package com.r0adkll.deckbuilder.arch.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.r0adkll.deckbuilder.arch.data.database.entities.PriceEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.ProductEntity

data class ProductWithPrices(
    @Embedded
    val product: ProductEntity,

    @Relation(entity = PriceEntity::class,
        parentColumn = "product_id",
        entityColumn = "parentId")
    val prices: List<PriceEntity>
)
