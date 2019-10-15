package com.r0adkll.deckbuilder.arch.data.features.marketplace

import com.r0adkll.deckbuilder.arch.data.features.marketplace.source.MarketplaceSource
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Product
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.repository.MarketplaceRepository
import com.r0adkll.deckbuilder.util.extensions.isToday
import io.reactivex.Observable
import java.lang.IllegalStateException

class CachingMarketplaceRepository(
        private val cacheSource: MarketplaceSource,
        private val networkSource: MarketplaceSource
) : MarketplaceRepository {

    override fun getPrice(cardId: String): Observable<List<Product>> {
        return cacheSource.getPrice(cardId)
                .flatMap { products ->
                    val oldestPrice = products.maxBy { it.recordedAt }
                    if (oldestPrice != null && oldestPrice.recordedAt.isToday()) {
                        Observable.just(products)
                    } else {
                        throw IllegalStateException("There doesn't appear to be an up to date price in the cache")
                    }
                }
                .onErrorResumeNext(networkSource.getPrice(cardId))
                .switchIfEmpty(networkSource.getPrice(cardId))
    }

    override fun getPrices(cardIds: List<String>): Observable<Map<String, Product>> {
        return cacheSource.getPrices(cardIds)
                .flatMap { products ->
                    if (products.values.any { !it.recordedAt.isToday() }) {
                        throw IllegalStateException("One of the prices appears to be out of date, force network pull")
                    } else {
                        Observable.just(products)
                    }
                }
                .onErrorResumeNext(networkSource.getPrices(cardIds))
                .switchIfEmpty(networkSource.getPrices(cardIds))
    }
}
