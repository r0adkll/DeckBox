package com.r0adkll.deckbuilder.arch.data.features.marketplace.mapper

import com.r0adkll.deckbuilder.arch.data.features.marketplace.model.PriceEntity
import com.r0adkll.deckbuilder.arch.data.features.marketplace.model.ProductEntity
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Price
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Product
import com.r0adkll.deckbuilder.util.extensions.milliseconds

object EntityMapper {

    fun from(entity: ProductEntity): Product {
        return Product(
                entity.cardId,
                entity.setCode,
                entity.groupId,
                entity.productId,
                entity.url,
                entity.prices.map { from(it) },
                entity.updatedAt.milliseconds
        )
    }

    fun from(entity: PriceEntity): Price {
        return Price(
                entity.subTypeName,
                entity.lowPrice,
                entity.midPrice,
                entity.highPrice,
                entity.marketPrice
        )
    }
}
