package com.r0adkll.deckbuilder.arch.domain.features.editing.repository

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Session
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import io.reactivex.Observable

/**
 * Repository for creating/managing deck editing
 */
interface EditRepository {

    /**
     * Create a new editing session. Either one with no deck, or a session that represents a deck
     *
     * @return the session id to refer too later
     */
    fun createSession(deck: Deck? = null, imports: List<PokemonCard>? = null): Observable<Long>

    /**
     * Get the editing session for the given [sessionId]
     */
    fun getSession(sessionId: Long): Observable<Session>

    /**
     * Observe the a session and all of it's changes
     */
    fun observeSession(sessionId: Long): Observable<Session>

    /**
     * Save the changes made during this session
     */
    fun persistSession(sessionId: Long): Observable<Unit>

    /**
     * Delete a session
     */
    fun deleteSession(sessionId: Long): Observable<Int>

    /**
     * Change/Add the deck image for the given session ID
     */
    fun changeDeckImage(sessionId: Long, image: DeckImage): Observable<Unit>

    /**
     * Chagne the deck's collection only flag
     */
    fun changeCollectionOnly(sessionId: Long, collectionOnly: Boolean): Observable<Unit>

    /**
     * Change the name of an editing session
     */
    fun changeName(sessionId: Long, name: String): Observable<String>

    /**
     * Change the description of a session
     */
    fun changeDescription(sessionId: Long, description: String): Observable<String>

    /**
     * Add cards to session
     */
    fun addCards(sessionId: Long, cards: List<PokemonCard>, searchSessionId: String? = null): Observable<Unit>

    /**
     * Remove card from a session
     */
    fun removeCard(sessionId: Long, card: PokemonCard, searchSessionId: String? = null): Observable<Unit>

    /**
     * Remove all the cards added in the search session for [searchSessionId]
     */
    fun clearSearchSession(sessionId: Long, searchSessionId: String): Observable<Unit>
}
