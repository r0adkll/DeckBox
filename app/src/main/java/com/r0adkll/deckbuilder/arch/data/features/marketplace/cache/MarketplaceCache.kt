package com.r0adkll.deckbuilder.arch.data.features.marketplace.cache

import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Product
import io.reactivex.Observable

interface MarketplaceCache {

    fun putPrices(prices: List<Product>)
    fun getPrice(cardId: String): Observable<Product>
    fun getPrices(cardIds: Set<String>): Observable<List<Product>>
    fun clear()
}
