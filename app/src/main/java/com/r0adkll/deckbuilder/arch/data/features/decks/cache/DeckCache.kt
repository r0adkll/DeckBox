package com.r0adkll.deckbuilder.arch.data.features.decks.cache

import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import io.reactivex.Observable

interface DeckCache {

    fun observeDeck(id: String): Observable<Deck>

    fun getDeck(id: String): Observable<Deck>

    fun getDecks(): Observable<List<Deck>>

    fun deleteDeck(deck: Deck): Observable<Unit>

    fun duplicateDeck(deck: Deck): Observable<Unit>
}
