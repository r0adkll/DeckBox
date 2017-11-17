package com.r0adkll.deckbuilder.arch.data.features.cards.repository.source


import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.cards.cache.ExpansionCache
import com.r0adkll.deckbuilder.arch.data.features.cards.cache.InMemoryExpansionCache
import com.r0adkll.deckbuilder.arch.data.features.cards.cache.PreferenceExpansionCache
import com.r0adkll.deckbuilder.arch.data.mappings.SetMapper
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.util.Schedulers
import io.pokemontcg.Pokemon
import io.reactivex.Observable
import javax.inject.Inject


class CachingCardDataSource @Inject constructor(
        val api: Pokemon,
        val preferences: AppPreferences,
        val schedulers: Schedulers
) : CardDataSource {

    private val memoryCache: ExpansionCache = InMemoryExpansionCache()
    private val diskCache: ExpansionCache = PreferenceExpansionCache(preferences)


    override fun getExpansions(): Observable<List<Expansion>> {
        return Observable.concat(memory(), disk(), network())
                .takeUntil { it.isNotEmpty() }
                .filter { it.isNotEmpty() }
    }


    private fun network(): Observable<List<Expansion>> {
        return api.set()
                .observeAll()
                .map { it.map { SetMapper.to(it) } }
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