package com.r0adkll.deckbuilder.arch.data.features.editing.repository


import com.r0adkll.deckbuilder.arch.data.features.editing.cache.SessionCache
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Session
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.util.Schedulers
import io.reactivex.Observable
import javax.inject.Inject


class DefaultEditRepository @Inject constructor(
        val cache: SessionCache,
        val decks: DeckRepository,
        val schedulers: Schedulers
) : EditRepository {

    override fun createSession(deck: Deck?, imports: List<PokemonCard>?): Observable<Long> {
        return cache.createSession(deck, imports)
                .subscribeOn(schedulers.database)
    }


    override fun getSession(sessionId: Long): Observable<Session> {
        return cache.getSession(sessionId)
                .subscribeOn(schedulers.database)
    }


    override fun persistSession(sessionId: Long): Observable<Unit> {
        return cache.getSession(sessionId)
                .flatMap { deck ->
                    decks.persistDeck(deck.deckId, deck.cards, deck.name, deck.description, deck.image)
                            .flatMap {
                                cache.resetSession(sessionId)
                                        .subscribeOn(schedulers.database)
                            }
                }
                .subscribeOn(schedulers.database)
    }


    override fun deleteSession(sessionId: Long): Observable<Int> {
        return cache.deleteSession(sessionId)
                .subscribeOn(schedulers.database)
    }


    override fun observeSession(sessionId: Long): Observable<Session> {
        return cache.observeSession(sessionId)
                .subscribeOn(schedulers.database)
    }


    override fun changeName(sessionId: Long, name: String): Observable<String> {
        return cache.changeName(sessionId, name)
                .subscribeOn(schedulers.database)
    }


    override fun changeDescription(sessionId: Long, description: String): Observable<String> {
        return cache.changeDescription(sessionId, description)
                .subscribeOn(schedulers.database)
    }


    override fun changeDeckImage(sessionId: Long, image: DeckImage): Observable<Unit> {
        return cache.changeDeckImage(sessionId, image)
                .subscribeOn(schedulers.database)
    }


    override fun addCards(sessionId: Long, cards: List<PokemonCard>, searchSessionId: String?): Observable<Unit> {
        return cache.addCards(sessionId, cards, searchSessionId)
                .subscribeOn(schedulers.database)
    }


    override fun removeCard(sessionId: Long, card: PokemonCard, searchSessionId: String?): Observable<Unit> {
        return cache.removeCard(sessionId, card, searchSessionId)
                .subscribeOn(schedulers.database)
    }


    override fun clearSearchSession(sessionId: Long, searchSessionId: String): Observable<Unit> {
        return cache.clearSearchSession(sessionId, searchSessionId)
                .subscribeOn(schedulers.database)
    }
}