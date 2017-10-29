package com.r0adkll.deckbuilder.arch.data.features.decks.repository

import com.r0adkll.deckbuilder.arch.data.features.decks.cache.DeckCache
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Inject


class DefaultDeckRepository @Inject constructor(
        val cache: DeckCache
) : DeckRepository {

    override fun createDeck(cards: List<PokemonCard>, name: String, description: String?): Observable<Deck> {
        return cache.putDeck(null, cards, name, description)
    }


    override fun updateDeck(id: String, cards: List<PokemonCard>, name: String, description: String?): Observable<Deck> {
        return cache.putDeck(id, cards, name, description)
    }


    override fun getDecks(): Observable<List<Deck>> {
        return cache.getDecks()
    }


    override fun deleteDeck(deck: Deck): Observable<Unit> {
        return cache.deleteDeck(deck)
    }
}