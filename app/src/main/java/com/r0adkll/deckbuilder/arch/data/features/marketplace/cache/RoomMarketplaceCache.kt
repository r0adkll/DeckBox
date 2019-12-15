package com.r0adkll.deckbuilder.arch.data.features.marketplace.cache

import com.r0adkll.deckbuilder.arch.data.database.DeckDatabase
import com.r0adkll.deckbuilder.arch.data.database.mapping.mapToModel
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Product
import com.r0adkll.deckbuilder.util.AppSchedulers
import io.reactivex.Observable
import javax.inject.Inject

class RoomMarketplaceCache @Inject constructor(
    val db: DeckDatabase,
    val schedulers: AppSchedulers
) : MarketplaceCache {

    override fun putPrices(prices: List<Product>) {
        db.marketplace().insertProducts(prices)
    }

    override fun getPrice(cardId: String): Observable<Product> {
        return db.marketplace().getProductWithPrices(cardId)
            .map { it.mapToModel() }
            .toObservable()
            .subscribeOn(schedulers.disk)
    }

    override fun getPrices(cardIds: Set<String>): Observable<List<Product>> {
        return db.marketplace().getLatestPriceForProducts(cardIds)
            .map { pricesWithProducts ->
                pricesWithProducts.map {
                    it.mapToModel()
                }
            }
            .toObservable()
            .subscribeOn(schedulers.disk)
    }

    override fun clear() {
        db.marketplace().deleteAll()
    }
}
