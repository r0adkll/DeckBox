package com.r0adkll.deckbuilder.arch.data.features.collection.repository

import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.collection.cache.FirestoreCollectionCache
import com.r0adkll.deckbuilder.arch.data.features.collection.cache.RoomCollectionCache
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.collection.model.CollectionCount
import com.r0adkll.deckbuilder.arch.domain.features.collection.repository.CollectionRepository
import io.reactivex.Observable


class DefaultCollectionRepository(
        private val roomCollectionCache: RoomCollectionCache,
        private val firestoreCollectionCache: FirestoreCollectionCache,
        private val preferences: AppPreferences
) : CollectionRepository {

    override fun observeAll(): Observable<List<CollectionCount>> = when(isOffline()) {
        true -> roomCollectionCache.observeAll()
        else -> firestoreCollectionCache.observeAll()
    }

    override fun getCount(cardId: String): Observable<CollectionCount> = when(isOffline()) {
        true -> roomCollectionCache.getCount(cardId)
        else -> firestoreCollectionCache.getCount(cardId)
    }

    override fun getCounts(cardIds: List<String>): Observable<List<CollectionCount>> = when(isOffline()) {
        true -> roomCollectionCache.getCounts(cardIds)
        else -> firestoreCollectionCache.getCounts(cardIds)
    }

    override fun getCountForSet(set: String): Observable<List<CollectionCount>> = when(isOffline()) {
        true -> roomCollectionCache.getCountForSet(set)
        else -> firestoreCollectionCache.getCountForSet(set)
    }

    override fun getCountForSeries(series: String): Observable<List<CollectionCount>> = when(isOffline()) {
        true -> roomCollectionCache.getCountForSeries(series)
        else -> firestoreCollectionCache.getCountForSeries(series)
    }

    override fun incrementCount(card: PokemonCard): Observable<Unit> = when(isOffline()) {
        true -> roomCollectionCache.incrementCount(card)
        else -> firestoreCollectionCache.incrementCount(card)
    }

    override fun decrementCount(card: PokemonCard): Observable<Unit> = when(isOffline()) {
        true -> roomCollectionCache.decrementCount(card)
        else -> firestoreCollectionCache.decrementCount(card)
    }

    private fun isOffline(): Boolean = preferences.offlineId.isSet && preferences.offlineId.get().isNotBlank()
}