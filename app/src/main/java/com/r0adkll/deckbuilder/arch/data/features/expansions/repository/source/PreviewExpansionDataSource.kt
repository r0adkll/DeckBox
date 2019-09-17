package com.r0adkll.deckbuilder.arch.data.features.expansions.repository.source

import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.expansions.cache.ExpansionCache
import com.r0adkll.deckbuilder.arch.data.features.expansions.cache.InMemoryExpansionCache
import com.r0adkll.deckbuilder.arch.data.features.expansions.cache.PreferenceExpansionCache
import com.r0adkll.deckbuilder.arch.data.mappings.SetMapper
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.internal.di.scopes.AppScope
import com.r0adkll.deckbuilder.util.AppSchedulers
import io.pokemontcg.Config
import io.pokemontcg.Pokemon
import io.reactivex.Observable
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import javax.inject.Inject

@AppScope
class PreviewExpansionDataSource @Inject constructor(
        val preferences: AppPreferences,
        val schedulers: AppSchedulers
) : ExpansionDataSource {

    private val previewApi = Pokemon(Config(
            logLevel = if (BuildConfig.DEBUG) BODY else NONE,
            apiUrl = "https://deckboxtcg.app/api/v1/"
    ))
    private val memoryCache: ExpansionCache = InMemoryExpansionCache()
    private val diskCache: ExpansionCache = PreferenceExpansionCache(preferences, AppPreferences::previewExpansions)

    override fun getExpansions(source: ExpansionCache.Source): Observable<List<Expansion>> {
        return when(source) {
            ExpansionCache.Source.ALL -> Observable.concat(memory(), disk(), network())
            ExpansionCache.Source.LOCAL -> Observable.concat(memory(), disk())
            ExpansionCache.Source.NETWORK -> network()
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
        return previewApi.set()
                .where {
                    pageSize = 200
                }
                .observeAll()
                .map { cardSets -> cardSets.map {
                    // Map network models to domain, flagging as preview expansion
                    SetMapper.to(it, true) }
                }
                .doOnNext { diskCache.putExpansions(it) }
                .doOnNext { memoryCache.putExpansions(it) }
                .subscribeOn(schedulers.network)
    }

    private fun disk(): Observable<List<Expansion>> {
        return diskCache.getExpansions()
                .doOnNext { memoryCache.putExpansions(it) }
    }

    private fun memory(): Observable<List<Expansion>> {
        return memoryCache.getExpansions()
    }
}
