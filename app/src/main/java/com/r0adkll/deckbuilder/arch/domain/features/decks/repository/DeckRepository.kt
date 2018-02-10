package com.r0adkll.deckbuilder.arch.domain.features.decks.repository


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import io.reactivex.Flowable
import io.reactivex.Observable


interface DeckRepository {

    fun getDeck(id: String): Observable<Deck>
    fun getDecks(): Observable<List<Deck>>
    fun createDeck(cards: List<PokemonCard>, name: String, description: String?): Observable<Deck>
    fun updateDeck(id: String, cards: List<PokemonCard>, name: String, description: String?): Observable<Deck>
    fun duplicateDeck(deck: Deck): Observable<Unit>
    fun deleteDeck(deck: Deck): Observable<Unit>
}