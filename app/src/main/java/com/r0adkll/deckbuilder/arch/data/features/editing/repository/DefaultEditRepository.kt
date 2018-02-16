package com.r0adkll.deckbuilder.arch.data.features.editing.repository


import com.r0adkll.deckbuilder.arch.data.features.editing.cache.SessionCache
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Session
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
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
                .subscribeOn(schedulers.disk)
    }


    override fun getSession(sessionId: Long): Observable<Session> {
        return cache.getSession(sessionId)
                .subscribeOn(schedulers.disk)
    }


    override fun persistSession(sessionId: Long): Observable<Unit> {
        return cache.getSession(sessionId)
                .flatMap {
                    decks.persistDeck(it.deckId, it.cards, it.name, it.description)
                            .flatMap { cache.resetSession(sessionId) }
                }
                .subscribeOn(schedulers.disk)
    }


    override fun deleteSession(sessionId: Long): Observable<Int> {
        return cache.deleteSession(sessionId)
                .subscribeOn(schedulers.disk)
    }


    override fun observeSession(sessionId: Long): Observable<Session> {
        return cache.observeSession(sessionId)
                .subscribeOn(schedulers.disk)
    }


    override fun changeName(sessionId: Long, name: String): Observable<String> {
        return cache.changeName(sessionId, name)
                .subscribeOn(schedulers.disk)
    }


    override fun changeDescription(sessionId: Long, description: String): Observable<String> {
        return cache.changeDescription(sessionId, description)
                .subscribeOn(schedulers.disk)
    }


    override fun addCards(sessionId: Long, cards: List<PokemonCard>): Observable<Unit> {
        return cache.addCards(sessionId, cards)
                .subscribeOn(schedulers.disk)
    }


    override fun removeCard(sessionId: Long, card: PokemonCard): Observable<Unit> {
        return cache.removeCard(sessionId, card)
                .subscribeOn(schedulers.disk)
    }


    override fun clearSearchSession(sessionId: Long, searchSessionId: String): Observable<Unit> {
        return cache.clearSearchSession(sessionId, searchSessionId)
                .subscribeOn(schedulers.disk)
    }
}