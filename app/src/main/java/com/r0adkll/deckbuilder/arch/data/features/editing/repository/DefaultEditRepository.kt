package com.r0adkll.deckbuilder.arch.data.features.editing.repository

import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.editing.source.EditSource
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import io.reactivex.Observable

class DefaultEditRepository(
    private val localSource: EditSource,
    private val remoteSource: EditSource,
    private val preferences: AppPreferences
) : EditRepository {

    private val isOffline: Boolean
        get() = preferences.offlineId.isSet && preferences.offlineId.get().isNotBlank()

    override fun startSession(imports: List<PokemonCard>?): Observable<Deck> = when (isOffline) {
        true -> localSource.startSession(imports)
        else -> remoteSource.startSession(imports)
    }

    override fun changeName(deckId: String, name: String): Observable<String> = when (isOffline) {
        true -> localSource.changeName(deckId, name)
        else -> remoteSource.changeName(deckId, name)
    }

    override fun changeDescription(deckId: String, description: String): Observable<String> = when (isOffline) {
        true -> localSource.changeDescription(deckId, description)
        else -> remoteSource.changeDescription(deckId, description)
    }

    override fun changeDeckImage(deckId: String, image: DeckImage): Observable<Unit> = when (isOffline) {
        true -> localSource.changeDeckImage(deckId, image)
        else -> remoteSource.changeDeckImage(deckId, image)
    }

    override fun changeCollectionOnly(deckId: String, collectionOnly: Boolean): Observable<Unit> = when (isOffline) {
        true -> localSource.changeCollectionOnly(deckId, collectionOnly)
        else -> remoteSource.changeCollectionOnly(deckId, collectionOnly)
    }

    override fun addCards(deckId: String, cards: List<PokemonCard>): Observable<Unit> = when (isOffline) {
        true -> localSource.addCards(deckId, cards)
        else -> remoteSource.addCards(deckId, cards)
    }

    override fun removeCard(deckId: String, card: PokemonCard): Observable<Unit> = when (isOffline) {
        true -> localSource.removeCard(deckId, card)
        else -> remoteSource.removeCard(deckId, card)
    }

    private fun <T> createSessionIfNeeded(deckId: String, action: (Deck?) -> Observable<T>): Observable<T> {
        return if (deckId == EditRepository.CREATE_DECK_ID) {
            startSession().flatMap { action(it) }
        } else {
            action(null)
        }
    }
}
