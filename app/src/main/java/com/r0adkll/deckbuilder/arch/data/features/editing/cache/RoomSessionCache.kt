package com.r0adkll.deckbuilder.arch.data.features.editing.cache

import com.r0adkll.deckbuilder.arch.data.database.DeckDatabase
import com.r0adkll.deckbuilder.arch.data.database.relations.StackedCard
import com.r0adkll.deckbuilder.arch.data.database.entities.SessionEntity
import com.r0adkll.deckbuilder.arch.data.database.relations.SessionWithChanges
import com.r0adkll.deckbuilder.arch.data.database.mapping.RoomEntityMapper
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Session
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.util.stack
import io.reactivex.Observable
import io.reactivex.functions.Function3
import java.io.IOException
import javax.inject.Inject


class RoomSessionCache @Inject constructor(
        val db: DeckDatabase,
        val cardRepository: CardRepository
) : SessionCache {


    override fun createSession(deck: Deck?, imports: List<PokemonCard>?): Observable<Long> {
        val newSession = createNewSession(deck)

        val cards = ArrayList<PokemonCard>()
        cards += deck?.cards ?: emptyList()
        cards += imports ?: emptyList()

        val stacks = cards.stack()
        val entities = stacks.map { RoomEntityMapper.to(it.card) }
        val joins = stacks.map { RoomEntityMapper.to(0L, it) }

        return Observable.fromCallable {
            db.sessions().insertNewSession(newSession, entities, joins)
        }
    }

    override fun getSession(sessionId: Long): Observable<Session> {
        return observeSession(sessionId)
                .take(1)
    }

    override fun observeSession(sessionId: Long): Observable<Session> {
        val session = db.sessions().getSessionWithChanges(sessionId).toObservable()
        val cards = db.sessions().getSessionCards(sessionId).toObservable()
        val expansions = cardRepository.getExpansions()

        return Observable.combineLatest(session, cards, expansions,
                Function3<SessionWithChanges, List<StackedCard>, List<Expansion>, Session> { s, c, e ->
                    RoomEntityMapper.to(s, c, e)
                })
    }

    override fun deleteSession(sessionId: Long): Observable<Int> {
        return Observable.fromCallable {
            db.sessions().deleteSession(sessionId)
        }
    }

    override fun resetSession(sessionId: Long): Observable<Unit> {
        return Observable.fromCallable {
            db.sessions().resetSession(sessionId)
        }
    }

    override fun changeName(sessionId: Long, name: String): Observable<String> {
        return Observable.create {
            val result = db.sessions().updateName(sessionId, name)
            if (!it.isDisposed) {
                if (result == 1) {
                    it.onNext(name)
                    it.onComplete()
                } else {
                    it.onError(IOException("No rows affected"))
                }
            }
        }
    }

    override fun changeDescription(sessionId: Long, description: String): Observable<String> {
        return Observable.create {
            val result = db.sessions().updateDescription(sessionId, description)
            if (!it.isDisposed) {
                if (result == 1) {
                    it.onNext(description)
                    it.onComplete()
                } else {
                    it.onError(IOException("No rows affected"))
                }
            }
        }
    }

    override fun changeDeckImage(sessionId: Long, image: DeckImage): Observable<Unit> {
        return Observable.create {
            val result = db.sessions().updateImage(sessionId, image.uri)
            if (!it.isDisposed) {
                if (result == 1) {
                    it.onNext(Unit)
                    it.onComplete()
                } else {
                    it.onError(IOException("No rows affected"))
                }
            }
        }
    }

    override fun addCards(sessionId: Long, cards: List<PokemonCard>, searchSessionId: String?): Observable<Unit> {
        val cardsToCache = cards.filter { !it.isCached }.map { RoomEntityMapper.to(it) }
        val changes = cards.map { RoomEntityMapper.createAddChange(sessionId, it, searchSessionId) }
        return Observable.fromCallable {
            db.sessions().insertAddChanges(sessionId, cardsToCache, changes)
        }
    }

    override fun removeCard(sessionId: Long, card: PokemonCard, searchSessionId: String?): Observable<Unit> {
        val cardToCache = if (!card.isCached) RoomEntityMapper.to(card) else null
        val change = RoomEntityMapper.createRemoveChange(sessionId, card, searchSessionId)
        return Observable.fromCallable {
            db.sessions().insertRemoveChange(sessionId, cardToCache, change)
        }
    }

    override fun clearSearchSession(sessionId: Long, searchSessionId: String): Observable<Unit> {
        return Observable.fromCallable {
            db.sessions().clearSearchSession(sessionId, searchSessionId)
        }
    }


    private fun createNewSession(deck: Deck?): SessionEntity {
        return SessionEntity(
                0L,
                deck?.id,
                deck?.name ?: "",
                deck?.description ?: "",
                deck?.image?.uri,
                deck?.name ?: "",
                deck?.description ?: "",
                deck?.image?.uri
        )
    }
}