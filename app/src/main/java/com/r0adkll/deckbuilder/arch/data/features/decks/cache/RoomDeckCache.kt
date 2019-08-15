package com.r0adkll.deckbuilder.arch.data.features.decks.cache

import com.r0adkll.deckbuilder.arch.data.database.DeckDatabase
import com.r0adkll.deckbuilder.arch.data.database.entities.DeckEntity
import com.r0adkll.deckbuilder.arch.data.database.mapping.RoomEntityMapper
import com.r0adkll.deckbuilder.arch.data.database.relations.DeckStackedCard
import com.r0adkll.deckbuilder.arch.data.database.relations.StackedCard
import com.r0adkll.deckbuilder.arch.data.features.expansions.ExpansionDataSource
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.security.InvalidParameterException
import javax.inject.Inject


class RoomDeckCache @Inject constructor(
        val db: DeckDatabase,
        val expansionSource: ExpansionDataSource
) : DeckCache {

    override fun getDeck(id: String): Observable<Deck> {
        val deckId = id.toLongOrNull()
        return if (deckId != null) {
            expansionSource.getExpansions()
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
        return expansionSource.getExpansions()
                .switchMap { expansions ->
                    val decks = db.decks().getDecks().toObservable()
                    val deckCards = db.decks().getDeckCards().toObservable()

                    Observable.combineLatest(decks, deckCards, BiFunction<List<DeckEntity>, List<DeckStackedCard>, List<Deck>> { d, c ->
                        d.map { deck ->
                            val cards = c.filter { it.deckId == deck.uid }
                            RoomEntityMapper.toDeck(deck, cards, expansions)
                        }
                    })
                }
    }

    override fun putDeck(
            id: String?,
            cards: List<PokemonCard>,
            name: String,
            description: String?,
            image: DeckImage?,
            collectionOnly: Boolean
    ): Observable<Deck> {
        return Observable.fromCallable {
            val entity = db.decks().insertDeckWithCards(id?.toLongOrNull(), cards, name, description, image, collectionOnly)
            Deck(entity.uid.toString(), name, description ?: "", image, collectionOnly, cards, false, entity.timestamp)
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

    fun deleteAll(){
        db.decks().deleteAll()
    }
}