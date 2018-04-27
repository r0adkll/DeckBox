package com.r0adkll.deckbuilder.arch.data.features.testing


import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.testing.DeckTester
import com.r0adkll.deckbuilder.arch.domain.features.testing.TestResults
import io.reactivex.Observable
import javax.inject.Inject


class DefaultDeckTester @Inject constructor(

): DeckTester {

    override fun testDeck(sessionId: Long, iterations: Int): Observable<TestResults> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun testDeck(deck: Deck, iterations: Int): Observable<TestResults> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}