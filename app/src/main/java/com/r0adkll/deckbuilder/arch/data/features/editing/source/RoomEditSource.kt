package com.r0adkll.deckbuilder.arch.data.features.editing.source

import com.r0adkll.deckbuilder.arch.data.database.DeckDatabase
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.util.AppSchedulers
import io.reactivex.Observable

class RoomEditSource(
    val db: DeckDatabase,
    val schedulers: AppSchedulers
) : EditSource {

    override fun startSession(imports: List<PokemonCard>?): Observable<Deck> {
        return Observable.fromCallable {
            val entity = db.decks().insertDeckWithCards(null, imports ?: emptyList(), "", null, null, false)
            Deck(
                entity.uid.toString(),
                "",
                "",
                null,
                false,
                imports ?: emptyList(),
                false,
                entity.timestamp
            )
        }.subscribeOn(schedulers.disk)
    }

    override fun changeName(deckId: String, name: String): Observable<String> {
        return db.sessions()
            .updateName(deckId, name)
            .toObservable()
            .map { name }
            .subscribeOn(schedulers.disk)
    }

    override fun changeDescription(deckId: String, description: String): Observable<String> {
        return db.sessions()
            .updateDescription(deckId, description)
            .toObservable()
            .map { description }
            .subscribeOn(schedulers.disk)
    }

    override fun changeDeckImage(deckId: String, image: DeckImage): Observable<Unit> {
        return db.sessions()
            .updateImage(deckId, image.uri)
            .toObservable()
            .map { Unit }
            .subscribeOn(schedulers.disk)
    }

    override fun changeCollectionOnly(deckId: String, collectionOnly: Boolean): Observable<Unit> {
        return db.sessions()
            .updateCollectionOnly(deckId, collectionOnly)
            .toObservable()
            .map { Unit }
            .subscribeOn(schedulers.disk)
    }

    override fun addCards(deckId: String, cards: List<PokemonCard>): Observable<Unit> {
        return Observable.fromCallable {
            db.sessions().addCards(deckId, cards)
        }.subscribeOn(schedulers.disk)
    }

    override fun removeCard(deckId: String, card: PokemonCard): Observable<Unit> {
        return Observable.fromCallable {
            db.sessions().removeCard(deckId, card)
        }.subscribeOn(schedulers.disk)
    }
}
