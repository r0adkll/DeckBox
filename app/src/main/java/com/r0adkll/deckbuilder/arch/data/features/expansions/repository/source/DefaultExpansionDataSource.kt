package com.r0adkll.deckbuilder.arch.data.features.expansions.repository.source

import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.expansions.cache.ExpansionCache
import com.r0adkll.deckbuilder.arch.data.features.expansions.cache.ExpansionCache.Source
import com.r0adkll.deckbuilder.arch.data.features.expansions.cache.InMemoryExpansionCache
import com.r0adkll.deckbuilder.arch.data.features.expansions.cache.PreferenceExpansionCache
import com.r0adkll.deckbuilder.arch.data.mappings.SetMapper
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.internal.di.scopes.AppScope
import com.r0adkll.deckbuilder.util.AppSchedulers
import io.pokemontcg.Pokemon
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("CheckResult")
@AppScope
class DefaultExpansionDataSource @Inject constructor(
    val api: Pokemon,
    val preferences: AppPreferences,
    val schedulers: AppSchedulers
) : ExpansionDataSource {

    private val memoryCache: ExpansionCache = InMemoryExpansionCache()
    private val diskCache: ExpansionCache = PreferenceExpansionCache(preferences, AppPreferences::expansions)

    override fun getExpansions(source: Source): Observable<List<Expansion>> {
        return when (source) {
            Source.ALL -> Observable.concat(memory(), disk(), network())
            Source.LOCAL -> Observable.concat(memory(), disk())
            Source.NETWORK -> network()
        }
            .takeUntil { it.isNotEmpty() }
            .filter { it.isNotEmpty() }
    }

    override fun refreshExpansions(): Observable<List<Expansion>> {
        // Force a refresh from the network, that will subsequently update cache implementations,
        // but won't clear on failure
        return network()
    }

    private fun network(): Observable<List<Expansion>> {
        return api.set()
            .where {
                pageSize = 200
            }
            .observeAll()
            .map { it.map { SetMapper.to(it) } }
            .doOnNext { diskCache.putExpansions(it) }
            .doOnNext { memoryCache.putExpansions(it) }
            .subscribeOn(schedulers.network)
            .doOnNext { Timber.d("Expansion::Network::getDecks():Thread(${Thread.currentThread().name})") }
    }

    private fun disk(): Observable<List<Expansion>> {
        return diskCache.getExpansions()
            .doOnNext { memoryCache.putExpansions(it) }
    }

    private fun memory(): Observable<List<Expansion>> {
        return memoryCache.getExpansions()
    }
}
