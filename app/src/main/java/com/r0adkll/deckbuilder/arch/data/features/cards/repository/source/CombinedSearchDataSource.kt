package com.r0adkll.deckbuilder.arch.data.features.cards.repository.source

import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import com.r0adkll.deckbuilder.arch.data.features.cards.cache.CardCache
import com.r0adkll.deckbuilder.arch.data.features.expansions.ExpansionDataSource
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.util.Schedulers
import com.r0adkll.deckbuilder.util.helper.Connectivity
import io.pokemontcg.Pokemon
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import io.reactivex.functions.Function
import javax.inject.Inject


class CombinedSearchDataSource(
        val preferences: AppPreferences,
        val diskSource: SearchDataSource,
        val networkSource: SearchDataSource,
        val connectivity: Connectivity
) : SearchDataSource {

    override fun search(type: SuperType?, query: String, filter: Filter?): Observable<List<PokemonCard>> {
        val forceDiskSearch = filter?.expansions
                ?.map { it.code }
                ?.let { it.isNotEmpty() && preferences.offlineExpansions.get().containsAll(it) }
                ?: false
        return if (connectivity.isConnected() && !forceDiskSearch) {
            networkSource.search(type, query, filter)
                    .onErrorResumeNext(Function {
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
