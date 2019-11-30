package com.r0adkll.deckbuilder.arch.data.database.mapping

import com.r0adkll.deckbuilder.arch.data.database.entities.PriceEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.ProductEntity
import com.r0adkll.deckbuilder.arch.data.database.relations.PriceWithProduct
import com.r0adkll.deckbuilder.arch.data.database.relations.ProductWithPrices
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Price
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Product

fun Product.mapToEntity(): ProductEntity {
    return ProductEntity(
        0L,
        cardId,
        setCode,
        groupId,
        productId,
        productName,
        url,
        modifiedOn
    )
}

fun Price.mapToEntity(productId: Long): PriceEntity {
    return PriceEntity(
        0L,
        rarity,
        low,
        mid,
        high,
        market,
        directLow,
        updatedAt,
        expiresAt,
        productId
    )
}

fun ProductWithPrices.mapToModel(): Product {
    return Product(
        product.cardId,
        product.setCode,
        product.groupId,
        product.productId,
        product.productName,
        product.url,
        prices.map { it.mapToModel() },
        prices.map { it.rarity }.toSet(),
        product.modifiedOn
    )
}

fun PriceWithProduct.mapToModel(): Product {
    return Product(
        product.cardId,
        product.setCode,
        product.groupId,
        product.productId,
        product.productName,
        product.url,
        listOf(price.mapToModel()),
        setOf(price.rarity),
        product.modifiedOn
    )
}

fun PriceEntity.mapToModel(): Price {
    return Price(
        rarity,
        low,
        mid,
        high,
        market,
        directLow,
        updatedAt,
        expiresAt
    )
}
