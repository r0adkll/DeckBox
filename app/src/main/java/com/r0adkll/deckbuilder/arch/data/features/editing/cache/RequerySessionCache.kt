package com.r0adkll.deckbuilder.arch.data.features.editing.cache


import com.r0adkll.deckbuilder.arch.data.features.editing.mapping.EntityMapper
import com.r0adkll.deckbuilder.arch.data.features.editing.model.*
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Session
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.requery.Persistable
import io.requery.reactivex.KotlinReactiveEntityStore
import timber.log.Timber
import javax.inject.Inject


class RequerySessionCache @Inject constructor(
        val db: KotlinReactiveEntityStore<Persistable>,
        val cardRepository: CardRepository
) : SessionCache {

    override fun createSession(deck: Deck?, imports: List<PokemonCard>?): Observable<Long> {
        val newSession = createNewSession(deck, imports)
        val newSessionObservable = db.insert(newSession)
                .map { it.id }
                .toObservable()
        if (deck != null) {
            return db.select(SessionEntity::class)
                    .where(SessionEntity.DECK_ID.eq(deck.id))
                    .get()
                    .observable()
                    .flatMap { existingSession ->
                        existingSession.originalName = deck.name
                        existingSession.originalDescription = deck.description
                        existingSession.originalImage = deck.image?.uri

                        db.update(existingSession)
                                .map { it.id }
                                .toObservable()
                    }
                    .switchIfEmpty(newSessionObservable)
        } else {
            return newSessionObservable
        }
    }


    private fun createNewSession(deck: Deck?, imports: List<PokemonCard>?): SessionEntity {
        val session = SessionEntity()
        session.deckId = deck?.id
        session.originalName = deck?.name ?: ""
        session.originalDescription = deck?.description ?: ""
        session.originalImage = deck?.image?.uri
        session.name = deck?.name ?: ""
        session.description = deck?.description ?: ""
        session.image = deck?.image?.uri

        val cards = ArrayList<ISessionCardEntity>()

        cards += deck?.cards?.map {
            EntityMapper.to(it)
        } ?: emptyList()

        cards += imports?.map {
            EntityMapper.to(it)
        } ?: emptyList()

        session.cards = cards
        return session
    }


    override fun getSession(sessionId: Long): Observable<Session> {
        val disk = db.select(SessionEntity::class)
                .where(SessionEntity.ID.eq(sessionId))
                .get()
                .observable()

        return Observable.combineLatest(disk, cardRepository.getExpansions(),
                BiFunction<SessionEntity, List<Expansion>, Session> { session, expansions ->
                    EntityMapper.to(session, expansions)
                })
    }


    override fun observeSession(sessionId: Long): Observable<Session> {
        return db.select(SessionEntity::class)
                .where(SessionEntity.ID.eq(sessionId))
                .limit(1)
                .get()
                .observableResult()
                .flatMap {
                    Observable.combineLatest(it.observable(), cardRepository.getExpansions(),
                            BiFunction<SessionEntity, List<Expansion>, Session> { session, expansions ->
                                EntityMapper.to(session, expansions)
                            })
                }
    }


    override fun deleteSession(sessionId: Long): Observable<Int> {
        return db.delete(SessionEntity::class).where(SessionEntity.ID.eq(sessionId))
                .get()
                .single()
                .toObservable()
    }


    override fun resetSession(sessionId: Long): Observable<Unit> {
        return db.select(SessionEntity::class)
                .where(SessionEntity.ID.eq(sessionId))
                .get()
                .observable()
                .flatMap { session ->
                    session.originalName = session.name
                    session.originalDescription = session.description
                    session.originalImage = session.image
                    (session.changes as java.util.List<IChangeEntity>).clear()
                    db.update(session).toObservable().map { Unit }
                }
    }


    override fun changeName(sessionId: Long, name: String): Observable<String> {
        return db.update(SessionEntity::class)
                .set(SessionEntity.NAME, name)
                .where(SessionEntity.ID.eq(sessionId))
                .get()
                .single()
                .toObservable()
                .map { name }
    }


    override fun changeDescription(sessionId: Long, description: String): Observable<String> {
        return db.update(SessionEntity::class)
                .set(SessionEntity.DESCRIPTION, description)
                .where(SessionEntity.ID.eq(sessionId))
                .get()
                .single()
                .toObservable()
                .map { description }
    }


    override fun changeDeckImage(sessionId: Long, image: DeckImage): Observable<Unit> {
        return db.update(SessionEntity::class)
                .set(SessionEntity.IMAGE, image.uri)
                .where(SessionEntity.ID.eq(sessionId))
                .get()
                .single()
                .toObservable()
                .map { Unit }
    }


    override fun addCards(sessionId: Long, cards: List<PokemonCard>, searchSessionId: String?): Observable<Unit> {
        return db.select(SessionEntity::class)
                .where(SessionEntity.ID.eq(sessionId))
                .get()
                .observable()
                .flatMap { session ->
                    val sessionCards = ArrayList<SessionCardEntity>()
                    val changes = ArrayList<ChangeEntity>()
                    cards.forEach {
                        changes += EntityMapper.createAddChange(it, searchSessionId)
                        sessionCards += EntityMapper.to(it)
                    }

                    Timber.i("Adding $sessionCards and $changes to $sessionId Session")

                    (session.cards as java.util.List<ISessionCardEntity>).addAll(sessionCards)
                    (session.changes as java.util.List<IChangeEntity>).addAll(changes)

                    db.update(session).toObservable().map { Unit }
                }
    }


    override fun removeCard(sessionId: Long, card: PokemonCard, searchSessionId: String?): Observable<Unit> {
        return db.select(SessionEntity::class)
                .where(SessionEntity.ID.eq(sessionId))
                .get()
                .observable()
                .flatMap { session ->
                    session.cards.firstOrNull { it.cardId == card.id }?.let {
                        (session.cards as java.util.List<ISessionCardEntity>).remove(it)
                        val change = EntityMapper.createRemoveChange(card, searchSessionId)
                        (session.changes as java.util.List<IChangeEntity>).add(change)
                        db.update(session).toObservable().map { Unit }
                    } ?: Observable.just(Unit)
                }
    }


    override fun clearSearchSession(sessionId: Long, searchSessionId: String): Observable<Unit> {
        return db.select(SessionEntity::class)
                .where(SessionEntity.ID.eq(sessionId))
                .get()
                .observable()
                .flatMap { session ->
                    val changes = session.changes.filter { it.searchSessionId == searchSessionId }
                            .groupBy { it.cardId }
                            .mapValues { it.value.sumBy { it.change } }
                            .filter { it.value > 0 }

                    changes.forEach { (cardId, count) ->
                        (0 until count).forEach { _ ->
                            session.cards.find { it.cardId == cardId }?.let {
                                (session.cards as java.util.List<ISessionCardEntity>).remove(it)
                            }
                        }
                    }

                    (session.changes as java.util.List<IChangeEntity>).removeAll { it.searchSessionId == searchSessionId }

                    db.update(session).toObservable().map { Unit }
                }
    }
}