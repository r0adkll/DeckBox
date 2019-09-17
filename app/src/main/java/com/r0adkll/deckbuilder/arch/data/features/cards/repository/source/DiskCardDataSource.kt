package com.r0adkll.deckbuilder.arch.data.features.cards.repository.source


import com.r0adkll.deckbuilder.arch.data.features.cards.cache.CardCache
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.util.AppSchedulers
import io.pokemontcg.model.SuperType
import io.reactivex.Observable


class DiskCardDataSource(
        val cache: CardCache,
        val schedulers: AppSchedulers
) : CardDataSource {

    override fun search(type: SuperType?, query: String, filter: Filter?): Observable<List<PokemonCard>> {
        return searchDisk(type, query, filter)
                .subscribeOn(schedulers.disk)
    }

    override fun find(ids: List<String>): Observable<List<PokemonCard>> {
        return cache.findCards(ids)
                .subscribeOn(schedulers.disk)
    }

    private fun searchDisk(type: SuperType?, query: String, filter: Filter?): Observable<List<PokemonCard>> {
        var searchFilter = filter

        if (type != null && type != SuperType.UNKNOWN) {
            searchFilter = searchFilter?.copy(superType = type)
        }

        return cache.findCards(query, searchFilter)
    }
}
