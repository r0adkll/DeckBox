package com.r0adkll.deckbuilder.arch.data.features.collection.source

import com.r0adkll.deckbuilder.arch.data.database.DeckDatabase
import com.r0adkll.deckbuilder.arch.data.features.collection.mapper.EntityMapper
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.collection.model.CollectionCount
import com.r0adkll.deckbuilder.util.AppSchedulers
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class RoomCollectionSource @Inject constructor(
        val db: DeckDatabase,
        val schedulers: AppSchedulers
) : CollectionSource {

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
            db.collection().incrementCount(card)!!
        }.subscribeOn(schedulers.database)
                .map { Unit }
    }

    override fun decrementCount(card: PokemonCard): Observable<Unit> {
        return Observable.fromCallable {
            db.collection().decrementCount(card)!!
        }.subscribeOn(schedulers.database)
                .map { Unit }
    }

    override fun incrementSet(set: String, cards: List<PokemonCard>): Observable<List<CollectionCount>> {
        return Observable.fromCallable {
            db.collection()
                    .incrementSet(set, cards)
                    .map(EntityMapper::to)
        }.subscribeOn(schedulers.database)
    }

    fun getAll(): Single<List<CollectionCount>> {
        return db.collection().getAll()
                .map { it.map(EntityMapper::to) }

    }

    fun deleteAll() {
        db.collection().deleteAll()
    }
}
