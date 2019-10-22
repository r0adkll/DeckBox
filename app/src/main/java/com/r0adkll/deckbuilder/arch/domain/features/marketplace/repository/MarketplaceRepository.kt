package com.r0adkll.deckbuilder.arch.domain.features.marketplace.repository

import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Product
import io.reactivex.Observable

interface MarketplaceRepository {

    fun getPrice(cardId: String): Observable<List<Product>>
    fun getPrices(cardIds: Set<String>): Observable<Map<String, Product>>
}
