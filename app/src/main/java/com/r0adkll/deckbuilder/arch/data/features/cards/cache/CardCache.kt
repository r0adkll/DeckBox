package com.r0adkll.deckbuilder.arch.data.features.cards.cache


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import io.pokemontcg.model.Card
import io.reactivex.Observable


interface CardCache {

    fun putCards(cards: List<Card>, isPreview: Boolean = false)
    fun findCards(ids: List<String>): Observable<List<PokemonCard>>
    fun findCards(query: String, filter: Filter?): Observable<List<PokemonCard>>
    fun clear()
}
