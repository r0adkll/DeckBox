package com.r0adkll.deckbuilder.arch.domain.features.editing.repository


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Session
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
    fun createSession(deck: Deck? = null): Observable<Long>


    /**
     * Get the editing session for the given [sessionId]
     */
    fun getSession(sessionId: Long): Observable<Session>


    /**
     * Delete a session
     */
    fun deleteSession(sessionId: Long): Observable<Int>


    /**
     * Observe the cards in a given session as they are updated
     */
    fun observeSessionCards(sessionId: Long): Observable<List<PokemonCard>>


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
    fun addCards(sessionId: Long, cards: List<PokemonCard>): Observable<Unit>


    /**
     * Remove card from a session
     */
    fun removeCard(sessionId: Long, card: PokemonCard): Observable<Unit>
}