package com.r0adkll.deckbuilder.arch.data.features.decks.cache


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import io.reactivex.Observable


interface DeckCache {

    fun getDecks(): Observable<List<Deck>>
    fun putDeck(id: String?, cards: List<PokemonCard>, name: String, description: String?) : Observable<Deck>
    fun deleteDeck(deck: Deck): Observable<Unit>
}