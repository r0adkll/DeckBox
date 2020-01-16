package com.r0adkll.deckbuilder.arch.data.features.collection.repository

import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.collection.source.FirestoreCollectionSource
import com.r0adkll.deckbuilder.arch.data.features.collection.source.RoomCollectionSource
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.collection.model.CollectionCount
import com.r0adkll.deckbuilder.arch.domain.features.collection.repository.CollectionRepository
import io.reactivex.Observable

class DefaultCollectionRepository(
    private val roomCollectionSource: RoomCollectionSource,
    private val firestoreCollectionSource: FirestoreCollectionSource,
    private val preferences: AppPreferences
) : CollectionRepository {

    override fun observeAll(): Observable<List<CollectionCount>> = when (isOffline()) {
        true -> roomCollectionSource.observeAll()
        else -> firestoreCollectionSource.observeAll()
    }

    override fun getCount(cardId: String): Observable<CollectionCount> = when (isOffline()) {
        true -> roomCollectionSource.getCount(cardId)
        else -> firestoreCollectionSource.getCount(cardId)
    }

    override fun getCountForSet(set: String): Observable<List<CollectionCount>> = when (isOffline()) {
        true -> roomCollectionSource.getCountForSet(set)
        else -> firestoreCollectionSource.getCountForSet(set)
    }

    override fun getCountForSeries(series: String): Observable<List<CollectionCount>> = when (isOffline()) {
        true -> roomCollectionSource.getCountForSeries(series)
        else -> firestoreCollectionSource.getCountForSeries(series)
    }

    override fun incrementCount(card: PokemonCard): Observable<Unit> = when (isOffline()) {
        true -> roomCollectionSource.incrementCount(card)
        else -> firestoreCollectionSource.incrementCount(card)
    }

    override fun decrementCount(card: PokemonCard): Observable<Unit> = when (isOffline()) {
        true -> roomCollectionSource.decrementCount(card)
        else -> firestoreCollectionSource.decrementCount(card)
    }

    override fun incrementSet(set: String, cards: List<PokemonCard>): Observable<List<CollectionCount>> =
        when (isOffline()) {
            true -> roomCollectionSource.incrementSet(set, cards)
            else -> firestoreCollectionSource.incrementSet(set, cards)
        }

    override fun decrementSet(set: String, cards: List<PokemonCard>): Observable<List<CollectionCount>> =
        when (isOffline()) {
            true -> roomCollectionSource.decrementSet(set, cards)
            else -> firestoreCollectionSource.decrementSet(set, cards)
        }

    private fun isOffline(): Boolean = preferences.offlineId.isSet && preferences.offlineId.get().isNotBlank()
}
