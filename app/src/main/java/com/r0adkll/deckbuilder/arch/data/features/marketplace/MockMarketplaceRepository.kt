package com.r0adkll.deckbuilder.arch.data.features.marketplace

import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Price
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.PriceCondition
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.PriceRarity
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.repository.MarketplaceRepository
import io.reactivex.Observable
import kotlin.random.Random

class MockMarketplaceRepository : MarketplaceRepository {

    override fun getRarities(): Observable<List<PriceRarity>> {
        return Observable.just(emptyList())
    }

    override fun getConditions(): Observable<List<PriceCondition>> {
        return Observable.just(emptyList())
    }

    override fun getPrice(cardId: String, rarity: String?): Observable<Price> {
        return Observable.just(
                Price(
                        cardId,
                        "Normal",
                        Random.nextDouble() * 100.0,
                        Random.nextDouble() * 100.0,
                        Random.nextDouble() * 100.0,
                        Random.nextDouble() * 100.0,
                        System.currentTimeMillis()
                )
        )
    }

    override fun getPriceHistory(cardId: String, rarity: String?): Observable<List<Price>> {
        return Observable.just(
                (0..7).map {
                    Price(
                            cardId,
                            "Normal",
                            Random.nextDouble() * 100.0,
                            Random.nextDouble() * 100.0,
                            Random.nextDouble() * 100.0,
                            Random.nextDouble() * 100.0,
                            System.currentTimeMillis()
                    )
                }
        )
    }

    override fun getPrices(cardIds: List<String>): Observable<Map<String, Price>> {
        return Observable.just(
                cardIds.map {
                    it to Price(
                            it,
                            "Normal",
                            Random.nextDouble() * 100.0,
                            Random.nextDouble() * 100.0,
                            Random.nextDouble() * 100.0,
                            Random.nextDouble() * 100.0,
                            System.currentTimeMillis()
                    )
                }.toMap()
        )
    }


}
