package com.r0adkll.deckbuilder.arch.domain.features.editing.repository

import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Edit
import io.reactivex.Observable

/**
 * Repository for creating/managing deck editing
 */
interface EditRepository {

    /**
     * Create a new session id for lazy-creating edits to a new deck
     * @return the new session id that is waiting to be mapped to a concrete deck id
     */
    fun createNewSession(): String

    /**
     * Observe changes in a deck for a given [editId]. This should return a cold stream of [Deck] updates for a given
     * session id and update when a new session [editId] get's mapped to a new deck id.
     * @return an observable stream of [Deck] changes
     */
    fun observeSession(editId: String): Observable<Deck>

    /**
     * Submit a deck edit request. If you pass a session id that hasn't been associated with a deck id, it
     * will create a new deck before performing the edit transaction
     *
     * @param editId the session id or a deck id to edit
     * @param request the [Edit] you want to make to a deck
     * @return an observable of the the actual DeckId that the changes are being made to. See [createNewSession]
     */
    fun submit(editId: String, request: Edit): Observable<String>
}
