package com.r0adkll.deckbuilder.arch.data.features.cards.repository.source.search

import com.r0adkll.deckbuilder.arch.data.Remote
import com.r0adkll.deckbuilder.arch.data.features.cards.cache.CardCache
import com.r0adkll.deckbuilder.arch.data.features.cards.repository.source.CardDataSource
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.util.Schedulers
import io.pokemontcg.Pokemon
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import javax.inject.Inject


class CombinedSearchDataSource @Inject constructor(
        val api: Pokemon,
        val cache: CardCache,
        val source: CardDataSource, // FIXME: I should probably come up with better naming/abstractions
        val remote: Remote,
        val schedulers: Schedulers
) : SearchDataSource {

    private val disk: SearchDataSource = DiskSearchDataSource(cache, schedulers)
    private val network: SearchDataSource = NetworkSearchDataSource(api, source, remote, schedulers)


    override fun search(type: SuperType?, query: String, filter: Filter?): Observable<List<PokemonCard>> {
        return Observable.concat(network.search(type, query, filter), disk.search(type, query, filter))
                .takeUntil { it.isNotEmpty() }
                .filter { it.isNotEmpty() }
    }


    override fun find(ids: List<String>): Observable<List<PokemonCard>> {
        return Observable.concat(network.find(ids), disk.find(ids))
                .takeUntil { it.isNotEmpty() }
                .filter { it.isNotEmpty() }
    }
}
