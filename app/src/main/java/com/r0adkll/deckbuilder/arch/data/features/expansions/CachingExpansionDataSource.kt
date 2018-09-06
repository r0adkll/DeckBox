package com.r0adkll.deckbuilder.arch.data.features.expansions


import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.Remote
import com.r0adkll.deckbuilder.arch.data.features.expansions.cache.ExpansionCache
import com.r0adkll.deckbuilder.arch.data.features.expansions.cache.InMemoryExpansionCache
import com.r0adkll.deckbuilder.arch.data.features.expansions.cache.PreferenceExpansionCache
import com.r0adkll.deckbuilder.arch.data.mappings.SetMapper
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.util.Schedulers
import io.pokemontcg.Pokemon
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("CheckResult")
class CachingExpansionDataSource @Inject constructor(
        val api: Pokemon,
        val preferences: AppPreferences,
        val schedulers: Schedulers,
        remote: Remote
) : ExpansionDataSource {

    private val memoryCache: ExpansionCache = InMemoryExpansionCache(remote)
    private val diskCache: ExpansionCache = PreferenceExpansionCache(preferences, remote)



    init {
        // Subscribe to any changes in the remote status and detect if we need to clear expansion cache
        remote.observeChanges()
                .subscribe {
                    val version = it.expansionVersion.split(".")
                    if (version.size == 2) {
                        val versionCode = version[0].toIntOrNull() ?: 1
                        val expansionCode = version[1]

                        val invalidCache = memoryCache.getExpansions().blockingFirst().none { it.code == expansionCode } ||
                                diskCache.getExpansions().blockingFirst().none { it.code == expansionCode }
                        if (versionCode > preferences.expansionsVersion || invalidCache) {
                            clearExpansions()
                            preferences.expansionsVersion = versionCode
                            Timber.i("Expansion Cache Invalidated (version: $versionCode, expansion: $expansionCode)")
                        }
                    }
                }
    }


    override fun getExpansions(): Observable<List<Expansion>> {
        return Observable.concat(memory(), disk(), network())
                .takeUntil { it.isNotEmpty() }
                .filter { it.isNotEmpty() }
    }


    override fun refreshExpansions(): Observable<List<Expansion>> {
        // Force a refresh from the network, that will subsequently update cache implementations,
        // but won't clear on failure
        return network()
    }


    override fun clearExpansions() {
        memoryCache.clear()
        diskCache.clear()
    }


    private fun network(): Observable<List<Expansion>> {
        return api.set()
                .observeAll()
                .map { it.map { SetMapper.to(it) } }
                .doOnNext { diskCache.putExpansions(it) }
                .doOnNext { memoryCache.putExpansions(it) }
                .subscribeOn(schedulers.network)
                .doOnNext { Timber.d("Expansion::Network::getDecks():Thread(${Thread.currentThread()?.name})") }
    }


    private fun disk(): Observable<List<Expansion>> {
        return diskCache.getExpansions()
                .doOnNext { memoryCache.putExpansions(it) }
    }


    private fun memory(): Observable<List<Expansion>> {
        return memoryCache.getExpansions()
    }
}