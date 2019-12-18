package com.r0adkll.deckbuilder.arch.domain.features.decks.repository

import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import io.reactivex.Observable

interface DeckRepository {

    fun getDeck(id: String): Observable<Deck>

    fun getDecks(): Observable<List<Deck>>

    fun duplicateDeck(deck: Deck): Observable<Unit>

    fun deleteDeck(deck: Deck): Observable<Unit>
}
