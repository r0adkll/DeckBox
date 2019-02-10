package com.r0adkll.deckbuilder.arch.data.features.collection.cache

import com.r0adkll.deckbuilder.arch.data.database.DeckDatabase
import com.r0adkll.deckbuilder.arch.data.features.collection.mapper.EntityMapper
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.collection.model.CollectionCount
import com.r0adkll.deckbuilder.util.Schedulers
import io.reactivex.Observable
import javax.inject.Inject


class RoomCollectionCache @Inject constructor(
        val db: DeckDatabase,
        val schedulers: Schedulers
) : CollectionCache {

    override fun observeAll(): Observable<List<CollectionCount>> {
        return db.collection().observeAll()
                .map { it.map(EntityMapper::to) }
                .toObservable()
    }

    override fun getCount(cardId: String): Observable<CollectionCount> {
        return db.collection()
                .getCount(cardId)
                .map(EntityMapper::to)
                .toObservable()
    }

    override fun getCounts(cardIds: List<String>): Observable<List<CollectionCount>> {
        return db.collection().getCounts(cardIds)
                .map { it.map(EntityMapper::to) }
                .toObservable()
    }

    override fun getCountForSet(set: String): Observable<List<CollectionCount>> {
        return db.collection().getCountForSet(set)
                .map { it.map(EntityMapper::to) }
                .toObservable()
    }

    override fun getCountForSeries(series: String): Observable<List<CollectionCount>> {
        return db.collection().getCountForSeries(series)
                .map { it.map(EntityMapper::to) }
                .toObservable()
    }

    override fun incrementCount(card: PokemonCard): Observable<Unit> {
        return Observable.fromCallable {
            db.collection().incrementCount(card)
        }.subscribeOn(schedulers.database)
    }

    override fun decrementCount(card: PokemonCard): Observable<Unit> {
        return Observable.fromCallable {
            db.collection().decrementCount(card)
        }.subscribeOn(schedulers.database)
    }
}