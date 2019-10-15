package com.r0adkll.deckbuilder.arch.data.features.marketplace.source

import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Product
import io.reactivex.Observable

interface MarketplaceSource {

    enum class Source {
        CACHE,
        NETWORK;
    }

    fun getPrice(cardId: String): Observable<List<Product>>

    fun getPrices(cardIds: List<String>): Observable<Map<String, Product>>
}
