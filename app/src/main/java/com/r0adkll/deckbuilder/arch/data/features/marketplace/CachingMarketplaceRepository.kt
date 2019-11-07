package com.r0adkll.deckbuilder.arch.data.features.marketplace

import com.r0adkll.deckbuilder.arch.data.features.marketplace.source.MarketplaceSource
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Product
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.repository.MarketplaceRepository
import com.r0adkll.deckbuilder.util.extensions.isToday
import io.reactivex.Observable

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
                    networkSource.getPrice(cardId)
                }
            }
            .onErrorResumeNext(networkSource.getPrice(cardId))
            .switchIfEmpty(networkSource.getPrice(cardId))
    }

    override fun getPrices(cardIds: Set<String>): Observable<Map<String, Product>> {
        return networkSource.getPrices(cardIds)
    }
}
