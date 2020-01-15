package com.r0adkll.deckbuilder.arch.domain.features.testing

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import io.reactivex.Observable

interface DeckTester {

    fun testDeck(deck: Deck, iterations: Int = 1000): Observable<TestResults>
    fun testDeckById(deckId: String, iterations: Int = 1000): Observable<TestResults>
    fun testHandById(deckId: String, iterations: Int = 7): Observable<List<PokemonCard>>
}
