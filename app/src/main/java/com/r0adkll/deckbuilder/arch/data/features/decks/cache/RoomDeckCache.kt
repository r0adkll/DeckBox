package com.r0adkll.deckbuilder.arch.data.features.decks.cache

import com.r0adkll.deckbuilder.arch.data.database.DeckDatabase
import com.r0adkll.deckbuilder.arch.data.database.entities.DeckEntity
import com.r0adkll.deckbuilder.arch.data.database.mapping.RoomEntityMapper
import com.r0adkll.deckbuilder.arch.data.database.relations.DeckStackedCard
import com.r0adkll.deckbuilder.arch.data.database.relations.StackedCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.expansions.repository.ExpansionRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.security.InvalidParameterException
import javax.inject.Inject

class RoomDeckCache @Inject constructor(
    val db: DeckDatabase,
    val repository: ExpansionRepository
) : DeckCache {

    override fun observeDeck(id: String): Observable<Deck> {
        val deckId = id.toLongOrNull()
        return if (deckId != null) {
            repository.getExpansions()
                .flatMap { expansions ->
                    db.decks().getDeck(deckId)
                        .flatMap { deckEntity ->
                            db.decks().getDeckCards(deckId)
                                .map { cards ->
                                    RoomEntityMapper.to(deckEntity, cards, expansions)
                                }
                        }
                        .toObservable()
                }
        } else {
            Observable.error(InvalidParameterException("'id' is not a valid deckId"))
        }
    }

    override fun getDeck(id: String): Observable<Deck> {
        val deckId = id.toLongOrNull()
        return if (deckId != null) {
            repository.getExpansions()
                .flatMap { expansions ->
                    val deck = db.decks().getDeck(deckId).toObservable()
                    val cards = db.decks().getDeckCards(deckId).toObservable()

                    Observable.combineLatest(deck, cards, BiFunction<DeckEntity, List<StackedCard>, Deck> { d, c ->
                        RoomEntityMapper.to(d, c, expansions)
                    })
                }
        } else {
            Observable.error(InvalidParameterException("'id' is not a valid deckId"))
        }
    }

    override fun getDecks(): Observable<List<Deck>> {
        return repository.getExpansions()
            .switchMap { expansions ->
                val decks = db.decks().getDecks().toObservable()
                val deckCards = db.decks().getDeckCards().toObservable()
                Observable.combineLatest(
                    decks,
                    deckCards,
                    BiFunction<List<DeckEntity>, List<DeckStackedCard>, List<Deck>> { d, c ->
                        d.map { deck ->
                            val cards = c.filter { it.deckId == deck.uid }
                            RoomEntityMapper.toDeck(deck, cards, expansions)
                        }
                    }
                )
            }
    }

    override fun deleteDeck(deck: Deck): Observable<Unit> {
        return Observable.fromCallable {
            db.decks().deleteDeck(deck.id.toLongOrNull() ?: 0L)
        }
    }

    override fun duplicateDeck(deck: Deck): Observable<Unit> {
        return Observable.fromCallable {
            db.decks().duplicateDeck(deck)
        }
    }

    fun deleteAll() {
        db.decks().deleteAll()
    }
}
