package com.r0adkll.deckbuilder.arch.data.features.marketplace

import com.r0adkll.deckbuilder.arch.data.features.marketplace.source.MarketplaceSource
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Product
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.repository.MarketplaceRepository
import com.r0adkll.deckbuilder.internal.di.scopes.AppScope
import io.reactivex.Observable
import javax.inject.Inject

@AppScope
class DefaultMarketplaceRepository @Inject constructor(
    val dataSource: MarketplaceSource
) : MarketplaceRepository {

    override fun getPrice(cardId: String): Observable<Product> {
        return dataSource.getPrice(cardId)
    }

    override fun getPrices(cardIds: Set<String>): Observable<Map<String, Product>> {
        return dataSource.getPrices(cardIds)
    }
}
