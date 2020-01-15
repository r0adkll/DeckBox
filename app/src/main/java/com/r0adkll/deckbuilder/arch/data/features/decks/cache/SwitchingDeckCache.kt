package com.r0adkll.deckbuilder.arch.data.features.decks.cache

import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import io.reactivex.Observable
import javax.inject.Inject

class SwitchingDeckCache @Inject constructor(
    val roomDeckCache: RoomDeckCache,
    val firestoreDeckCache: FirestoreDeckCache,
    val preferences: AppPreferences
) : DeckCache {

    override fun observeDeck(id: String): Observable<Deck> = when (isOffline()) {
        true -> roomDeckCache.observeDeck(id)
        else -> firestoreDeckCache.observeDeck(id)
    }

    override fun getDeck(id: String): Observable<Deck> = when (isOffline()) {
        true -> roomDeckCache.getDeck(id)
        else -> firestoreDeckCache.getDeck(id)
    }

    override fun getDecks(): Observable<List<Deck>> = preferences.offlineId.asObservable().switchMap {
        when (isOffline()) {
            true -> roomDeckCache.getDecks()
            else -> firestoreDeckCache.getDecks()
        }
    }

    override fun deleteDeck(deck: Deck): Observable<Unit> = when (isOffline()) {
        true -> roomDeckCache.deleteDeck(deck)
        else -> firestoreDeckCache.deleteDeck(deck)
    }

    override fun duplicateDeck(deck: Deck): Observable<Unit> = when (isOffline()) {
        true -> roomDeckCache.duplicateDeck(deck)
        else -> firestoreDeckCache.duplicateDeck(deck)
    }

    private fun isOffline(): Boolean = preferences.offlineId.isSet && preferences.offlineId.get().isNotBlank()
}
