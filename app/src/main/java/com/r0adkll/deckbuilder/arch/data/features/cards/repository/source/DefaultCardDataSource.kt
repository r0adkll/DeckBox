package com.r0adkll.deckbuilder.arch.data.features.cards.repository.source

import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import com.r0adkll.deckbuilder.util.helper.Connectivity
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import io.reactivex.functions.Function
import timber.log.Timber

class DefaultCardDataSource(
    val preferences: AppPreferences,
    val diskSource: CardDataSource,
    val networkSource: CardDataSource,
    val connectivity: Connectivity,
    val remote: Remote
) : CardDataSource {

    override fun findByExpansion(setCode: String): Observable<List<PokemonCard>> {
        val forceDiskSearch = preferences.offlineExpansions.get().contains(setCode)
        return if (connectivity.isConnected() && !forceDiskSearch) {
            networkSource.findByExpansion(setCode)
                .onErrorResumeNext(Function {
                    Timber.e(it, "Error fetching network cards for $setCode")
                    diskSource.findByExpansion(setCode)
                })
        } else {
            diskSource.findByExpansion(setCode)
        }
    }

    override fun search(type: SuperType?, query: String, filter: Filter?): Observable<List<PokemonCard>> {
        val filterExpansionCodes = filter?.expansions?.map { it.code }
        val forceDiskSearch = filterExpansionCodes
            ?.let { it.isNotEmpty() && preferences.offlineExpansions.get().containsAll(it) }
            ?: false

        return if (connectivity.isConnected() && !forceDiskSearch) {
            networkSource.search(type, query, filter)
                .onErrorResumeNext(Function {
                    Timber.e(it, "Error searching for cards")
                    diskSource.search(type, query, filter)
                })
        } else {
            diskSource.search(type, query, filter)
        }
    }

    override fun find(ids: List<String>): Observable<List<PokemonCard>> {
        return if (connectivity.isConnected()) {
            diskSource.find(ids)
                .flatMap { diskCards ->
                    val missingIds = ids.filter { id -> diskCards.none { card -> card.id == id } }
                    if (missingIds.isNotEmpty()) {
                        networkSource.find(missingIds)
                            .map { it.plus(diskCards) }
                    } else {
                        Observable.just(diskCards)
                    }
                }
        } else {
            diskSource.find(ids)
        }
    }
}
