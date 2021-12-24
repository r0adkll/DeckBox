package com.r0adkll.deckbuilder.arch.data.features.marketplace.source

import com.r0adkll.deckbuilder.arch.data.features.marketplace.api.TcgReplayer
import com.r0adkll.deckbuilder.arch.data.features.marketplace.api.mapping.mapToModel
import com.r0adkll.deckbuilder.arch.data.features.marketplace.api.mapping.mapToModels
import com.r0adkll.deckbuilder.arch.data.features.marketplace.cache.MarketplaceCache
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Product
import com.r0adkll.deckbuilder.util.AppSchedulers
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class TcgReplayerMarketplaceSource @Inject constructor(
    val api: TcgReplayer,
    val cache: MarketplaceCache,
    val schedulers: AppSchedulers
) : MarketplaceSource {

    override fun getPrice(cardId: String): Observable<Product> {
        return Observable.concat(cache(cardId), network(cardId))
            .takeUntil {
                val expiresAt = it.prices.maxByOrNull { it.expiresAt }?.expiresAt ?: 0L
                expiresAt > System.currentTimeMillis()
            }
    }

    override fun getPrices(cardIds: Set<String>): Observable<Map<String, Product>> {
        return Observable.concat(cache(cardIds), network(cardIds))
            .takeUntil {
                it.keys.containsAll(cardIds) &&
                    it.none {
                        val expiresAt = it.value.prices.maxByOrNull { it.expiresAt }?.expiresAt ?: 0L
                        expiresAt < System.currentTimeMillis()
                    }
            }
    }

    private fun cache(cardId: String): Observable<Product> {
        return cache.getPrice(cardId)
            .onErrorResumeNext(Observable.empty())
            .subscribeOn(schedulers.disk)
    }

    private fun cache(cardIds: Set<String>): Observable<Map<String, Product>> {
        return cache.getPrices(cardIds)
            .map {
                it.map { product ->
                    product.cardId to product
                }.toMap()
            }
            .doOnNext {
                Timber.d("Prices from Cache: $it")
            }
            .onErrorReturnItem(emptyMap())
            .subscribeOn(schedulers.disk)
    }

    private fun network(cardId: String): Observable<Product> {
        return api.getPrice(cardId)
            .map { it.mapToModel() }
            .doOnNext {
                cache.putPrices(listOf(it))
            }
            .onErrorResumeNext(Observable.empty())
            .subscribeOn(schedulers.network)
    }

    private fun network(cardIds: Set<String>): Observable<Map<String, Product>> {
        return api.getPrices(cardIds)
            .map { it.mapToModels() }
            .doOnNext {
                cache.putPrices(it)
            }
            .map { products ->
                products.map {
                    it.cardId to it
                }.toMap()
            }
    }
}
