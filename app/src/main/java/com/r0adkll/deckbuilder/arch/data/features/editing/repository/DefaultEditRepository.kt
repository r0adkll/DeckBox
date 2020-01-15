package com.r0adkll.deckbuilder.arch.data.features.editing.repository

import androidx.annotation.VisibleForTesting
import com.jakewharton.rxrelay2.PublishRelay
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.editing.source.EditSource
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Edit
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import io.reactivex.Observable
import java.util.*

class DefaultEditRepository(
    private val localSource: EditSource,
    private val remoteSource: EditSource,
    private val preferences: AppPreferences,
    private val deckRepository: DeckRepository
) : EditRepository {

    private val changeNotifications = PublishRelay.create<Unit>()

    private val isOffline: Boolean
        get() = preferences.offlineId.isSet && preferences.offlineId.get().isNotBlank()

    /**
     * This is a map of session ids => deck ids. This keeps track of our lazy-created deck for editing so that
     * the UI doesn't have to manage updating and created deck ids and can instead just keep passing it's session, or
     * deck id
     */
    @VisibleForTesting
    val sessions = mutableMapOf<String, String?>()

    override fun createNewSession(): String {
        val newSessionId = UUID.randomUUID().toString()
        sessions[newSessionId] = null
        return newSessionId
    }

    override fun observeSession(editId: String): Observable<Deck> {
        return if (sessions.containsKey(editId)) {
            val mappedDeckId = sessions[editId]
            if (mappedDeckId.isNullOrBlank()) {
                // At this point, we are waiting on the user to make an edit, so we want to switchMap the sessions change notifications
                changeNotifications.switchMap {
                    // Check again if the mapped deck id exists for this edit id, if so observe it, otherwise just return empty
                    val newMappedDeckId = sessions[editId]
                    if (newMappedDeckId.isNullOrBlank()) {
                        Observable.empty()
                    } else {
                        deckRepository.observeDeck(newMappedDeckId)
                    }
                }
            } else {
                deckRepository.observeDeck(mappedDeckId)
            }
        } else {
            deckRepository.observeDeck(editId)
        }
    }

    override fun submit(editId: String, request: Edit): Observable<String> = createDeckIfNeeded(editId) { deckId ->
        when (request) {
            is Edit.Name -> changeName(deckId, request.name)
            is Edit.Image -> changeDeckImage(deckId, request.image)
            is Edit.Description -> changeDescription(deckId, request.description)
            is Edit.CollectionOnly -> changeCollectionOnly(deckId, request.collectionOnly)
            is Edit.AddCards -> addCards(deckId, request.cards)
            is Edit.RemoveCard -> removeCard(deckId, request.card)
        }.map { deckId }
    }

    private fun startSession(imports: List<PokemonCard>? = null): Observable<Deck> = when (isOffline) {
        true -> localSource.startSession(imports)
        else -> remoteSource.startSession(imports)
    }

    private fun changeName(deckId: String, name: String): Observable<String> = when (isOffline) {
        true -> localSource.changeName(deckId, name)
        else -> remoteSource.changeName(deckId, name)
    }

    private fun changeDescription(deckId: String, description: String): Observable<String> = when (isOffline) {
        true -> localSource.changeDescription(deckId, description)
        else -> remoteSource.changeDescription(deckId, description)
    }

    private fun changeDeckImage(deckId: String, image: DeckImage): Observable<Unit> = when (isOffline) {
        true -> localSource.changeDeckImage(deckId, image)
        else -> remoteSource.changeDeckImage(deckId, image)
    }

    private fun changeCollectionOnly(deckId: String, collectionOnly: Boolean): Observable<Unit> = when (isOffline) {
        true -> localSource.changeCollectionOnly(deckId, collectionOnly)
        else -> remoteSource.changeCollectionOnly(deckId, collectionOnly)
    }

    private fun addCards(deckId: String, cards: List<PokemonCard>): Observable<Unit> = when (isOffline) {
        true -> localSource.addCards(deckId, cards)
        else -> remoteSource.addCards(deckId, cards)
    }

    private fun removeCard(deckId: String, card: PokemonCard): Observable<Unit> = when (isOffline) {
        true -> localSource.removeCard(deckId, card)
        else -> remoteSource.removeCard(deckId, card)
    }

    /**
     * Create a new deck if needed. If the [editId] passed is not a set key in the sessions map then we can only
     * assume that it is a valid deck id and should let it pass through to the edit. If the [editId] is set in the
     * sessions map but doesn't have a mapped deckId, then we need to create a new deck and associate the mapping.
     * If the [editId] is set in the session map, and has a valid deckId, then we just pass through that mapped
     * deck id.
     *
     * @param editId the id that the user is making the edit with. Could be a new session id, a mapped session id, or a deck id
     * @param action the resulting edit action to apply after determining if we need a new deck or not
     */
    private fun <T> createDeckIfNeeded(editId: String, action: (String) -> Observable<T>): Observable<T> {
        return if (sessions.containsKey(editId)) {
            // Synchronize this call so that if multiple edits get submitted we don't generate multiple deck instances
            synchronized(this@DefaultEditRepository) {
                val mappedDeckId = sessions[editId]
                if (mappedDeckId.isNullOrBlank()) {
                    startSession().flatMap { newDeck ->
                        sessions[editId] = newDeck.id
                        changeNotifications.accept(Unit)
                        action(newDeck.id)
                    }
                } else {
                    action(mappedDeckId)
                }
            }
        } else {
            action(editId)
        }
    }
}
