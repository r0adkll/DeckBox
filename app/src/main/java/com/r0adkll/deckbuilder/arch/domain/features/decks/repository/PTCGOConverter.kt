package com.r0adkll.deckbuilder.arch.domain.features.decks.repository


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import io.reactivex.Observable


interface PTCGOConverter {

    fun import(deckList: String): Observable<List<PokemonCard>>
    fun export(deck: Deck): Observable<String>
}