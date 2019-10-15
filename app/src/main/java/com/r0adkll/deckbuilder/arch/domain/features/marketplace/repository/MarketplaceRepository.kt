package com.r0adkll.deckbuilder.arch.domain.features.marketplace.repository

import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Product
import io.reactivex.Observable

interface MarketplaceRepository {

    fun getPrice(cardId: String): Observable<List<Product>>
    fun getPrices(cardIds: List<String>): Observable<Map<String, Product>>
}
