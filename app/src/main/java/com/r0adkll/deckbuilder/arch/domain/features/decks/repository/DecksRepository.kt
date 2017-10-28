package com.r0adkll.deckbuilder.arch.domain.features.decks.repository


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import io.reactivex.Observable


interface DecksRepository {

    fun getDecks(): Observable<List<Deck>>
    fun createDeck(cards: List<PokemonCard>, name: String, description: String?): Observable<Deck>
    fun updateDeck(id: Long, cards: List<PokemonCard>, name: String, description: String?): Observable<Deck>
    fun deleteDeck(deck: Deck): Observable<List<Deck>>
}