package com.r0adkll.deckbuilder.arch.data.features.decks.repository

import com.r0adkll.deckbuilder.arch.data.features.decks.cache.DeckCache
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.util.AppSchedulers
import io.reactivex.Observable
import javax.inject.Inject

class DefaultDeckRepository @Inject constructor(
        val cache: DeckCache,
        val schedulers: AppSchedulers
) : DeckRepository {

    override fun getDeck(id: String): Observable<Deck> {
        return cache.getDeck(id)
                .subscribeOn(schedulers.firebase)
    }

    override fun getDecks(): Observable<List<Deck>> {
        return cache.getDecks()
//                .subscribeOn(schedulers.firebase)
    }

    override fun persistDeck(
            id: String?,
            cards: List<PokemonCard>,
            name: String,
            description: String?,
            image: DeckImage?,
            collectionOnly: Boolean
    ): Observable<Deck> {
        return cache.putDeck(id, cards, name, description, image, collectionOnly)
                .subscribeOn(schedulers.firebase)
    }

    override fun duplicateDeck(deck: Deck): Observable<Unit> {
        return cache.duplicateDeck(deck)
                .subscribeOn(schedulers.firebase)
    }

    override fun deleteDeck(deck: Deck): Observable<Unit> {
        return cache.deleteDeck(deck)
                .subscribeOn(schedulers.firebase)
    }
}
