package com.r0adkll.deckbuilder.arch.data.features.cards.repository.source

import com.r0adkll.deckbuilder.arch.data.remote.Remote
import com.r0adkll.deckbuilder.arch.data.features.cards.cache.CardCache
import com.r0adkll.deckbuilder.arch.data.features.expansions.ExpansionDataSource
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.util.Schedulers
import com.r0adkll.deckbuilder.util.helper.Connectivity
import io.pokemontcg.Pokemon
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import javax.inject.Inject


class CombinedSearchDataSource @Inject constructor(
        val api: Pokemon,
        val cache: CardCache,
        val source: ExpansionDataSource,
        val remote: Remote,
        val schedulers: Schedulers,
        val connectivity: Connectivity
) : SearchDataSource {

    private val disk: SearchDataSource = DiskSearchDataSource(cache, schedulers)
    private val network: SearchDataSource = CachingNetworkSearchDataSource(api, source, cache, remote, schedulers)


    override fun search(type: SuperType?, query: String, filter: Filter?): Observable<List<PokemonCard>> {
        return if (connectivity.isConnected()) {
            network.search(type, query, filter)
                    .onErrorResumeNext(disk.search(type, query, filter))
        } else {
            disk.search(type, query, filter)
        }
    }


    override fun find(ids: List<String>): Observable<List<PokemonCard>> {
        return if (connectivity.isConnected()) {
            network.find(ids)
                    .onErrorResumeNext(disk.find(ids))
        } else {
            disk.find(ids)
        }
    }
}
