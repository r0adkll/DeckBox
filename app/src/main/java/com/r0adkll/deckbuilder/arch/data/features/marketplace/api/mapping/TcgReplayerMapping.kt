package com.r0adkll.deckbuilder.arch.data.features.marketplace.api.mapping

import com.r0adkll.deckbuilder.arch.data.features.marketplace.api.model.ApiListPriceResponse
import com.r0adkll.deckbuilder.arch.data.features.marketplace.api.model.ApiPriceResponse
import com.r0adkll.deckbuilder.arch.data.features.marketplace.api.model.CardPricing
import com.r0adkll.deckbuilder.arch.data.features.marketplace.api.model.PriceResponse
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Price
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Product
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter

fun ApiPriceResponse.mapToModel(): Product {
    val nextScheduledAt = nextScheduledTime.toEpochMillis()
    return price.mapToModel(nextScheduledAt)
}

fun ApiListPriceResponse.mapToModels(): List<Product> {
    val nextScheduledAt = nextScheduledTime.toEpochMillis()
    return prices.map {
        it.mapToModel(nextScheduledAt)
    }
}

fun PriceResponse.mapToModel(nextScheduledAt: Long): Product {
    return Product(
        cardId,
        setCode,
        groupId,
        productId,
        productName,
        url,
        prices?.map {
            it.mapToModel(nextScheduledAt)
        } ?: emptyList(),
        prices?.map { it.subTypeName }?.toSet() ?: emptySet(),
        modifiedOn.toEpochMillis()
    )
}

fun CardPricing.mapToModel(nextScheduledAt: Long): Price {
    return Price(
        subTypeName,
        lowPrice,
        midPrice,
        highPrice,
        marketPrice,
        directLowPrice,
        updatedAt.toEpochMillis(),
        nextScheduledAt
    )
}

fun String.toEpochMillis(): Long {
    return LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
        .toInstant(ZoneOffset.UTC)
        .toEpochMilli()
}
