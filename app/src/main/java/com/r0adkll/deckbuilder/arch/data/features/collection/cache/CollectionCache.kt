package com.r0adkll.deckbuilder.arch.data.features.collection.cache

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.collection.model.CollectionCount
import io.reactivex.Observable


interface CollectionCache {

    fun observeAll(): Observable<List<CollectionCount>>

    fun getCount(cardId: String): Observable<CollectionCount>
    fun getCountForSet(set: String): Observable<List<CollectionCount>>
    fun getCountForSeries(series: String): Observable<List<CollectionCount>>

    fun incrementCount(card: PokemonCard): Observable<Unit>
    fun decrementCount(card: PokemonCard): Observable<Unit>
    fun incrementCounts(cards: List<PokemonCard>): Observable<Unit>
}