package com.r0adkll.deckbuilder.arch.domain.features.decks.repository


import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import io.reactivex.Observable


interface DecksRepository {

    fun getDecks(): Observable<List<Deck>>
    fun deleteDeck(deck: Deck): Observable<List<Deck>>
}