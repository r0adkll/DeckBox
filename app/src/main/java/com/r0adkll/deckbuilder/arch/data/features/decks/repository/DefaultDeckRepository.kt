package com.r0adkll.deckbuilder.arch.data.features.decks.repository

import com.r0adkll.deckbuilder.arch.data.features.decks.cache.DeckCache
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.util.Schedulers
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Inject


class DefaultDeckRepository @Inject constructor(
        val cache: DeckCache,
        val schedulers: Schedulers
) : DeckRepository {

    override fun getDeck(id: String): Observable<Deck> {
        return cache.getDeck(id)
                .subscribeOn(schedulers.disk)
    }


    override fun getDecks(): Observable<List<Deck>> {
        return cache.getDecks()
                .subscribeOn(schedulers.disk)
    }


    override fun persistDeck(id: String?, cards: List<PokemonCard>, name: String, description: String?): Observable<Deck> {
        return cache.putDeck(id, cards, name, description)
                .subscribeOn(schedulers.disk)
    }


    override fun duplicateDeck(deck: Deck): Observable<Unit> {
        return cache.duplicateDeck(deck)
                .subscribeOn(schedulers.disk)
    }


    override fun deleteDeck(deck: Deck): Observable<Unit> {
        return cache.deleteDeck(deck)
                .subscribeOn(schedulers.disk)
    }
}