package com.r0adkll.deckbuilder.arch.data.features.editing.cache


import com.r0adkll.deckbuilder.arch.data.features.editing.mapping.EntityMapper
import com.r0adkll.deckbuilder.arch.data.features.editing.model.SessionCardEntity
import com.r0adkll.deckbuilder.arch.data.features.editing.model.SessionEntity
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Session
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.requery.Persistable
import io.requery.reactivex.KotlinReactiveEntityStore
import javax.inject.Inject


class RequerySessionCache @Inject constructor(
        val db: KotlinReactiveEntityStore<Persistable>,
        val cardRepository: CardRepository
) : SessionCache {

    override fun createSession(deck: Deck?): Observable<Long> {
        val session = SessionEntity()
        session.deckId = deck?.id
        session.name = deck?.name
        session.description = deck?.description

        val cards = deck?.cards?.map {
            EntityMapper.to(session, it)
        }

        session.cards = cards

        return db.upsert(session)
                .map { it.id }
                .toObservable()
    }


    override fun getSession(sessionId: Long): Observable<Session> {
        return db.select(SessionEntity::class)
                .where(SessionEntity.ID.eq(sessionId))
                .get()
                .observable()
                .map { EntityMapper.to(it) }
    }


    override fun deleteSession(sessionId: Long): Observable<Int> {
        return db.delete(SessionEntity::class).where(SessionEntity.ID.eq(sessionId))
                .get()
                .single()
                .toObservable()
    }


    override fun observeSessionCards(sessionId: Long): Observable<List<PokemonCard>> {
        return db.select(SessionCardEntity::class)
                .join(SessionEntity::class).on(SessionEntity.ID.eq(SessionCardEntity.SESSION_ID))
                .where(SessionEntity.ID.eq(sessionId))
                .get()
                .observableResult()
                .flatMap {
                    Observable.combineLatest(it.observable().toList().toObservable(), cardRepository.getExpansions(),
                            BiFunction<List<SessionCardEntity>, List<Expansion>, List<PokemonCard>> { cards, expansions ->
                                EntityMapper.from(expansions, cards)
                            })
                }
    }


    override fun changeName(sessionId: Long, name: String): Observable<String> {
        return db.update(SessionEntity::class)
                .set(SessionEntity.NAME, name)
                .get()
                .single()
                .toObservable()
                .map { name }
    }


    override fun changeDescription(sessionId: Long, description: String): Observable<String> {
        return db.update(SessionEntity::class)
                .set(SessionEntity.DESCRIPTION, description)
                .get()
                .single()
                .toObservable()
                .map { description }
    }


    override fun addCards(sessionId: Long, cards: List<PokemonCard>): Observable<Unit> {
        return db.select(SessionEntity::class)
                .where(SessionEntity.ID.eq(sessionId))
                .get()
                .observable()
                .flatMap { session ->
                    val sessionCards = cards.map {
                        EntityMapper.to(session, it)
                    }
                    db.insert(sessionCards).toObservable().map { Unit }
                }
    }


    override fun removeCard(sessionId: Long, card: PokemonCard): Observable<Unit> {
        return db.delete(SessionCardEntity::class)
                .join(SessionEntity::class).on(SessionEntity.ID.eq(SessionCardEntity.SESSION_ID))
                .where(SessionEntity.ID.eq(sessionId))
                .and(SessionCardEntity.CARD_ID.eq(card.id))
                .limit(1)
                .get()
                .single()
                .toObservable()
                .map { Unit }
    }
}