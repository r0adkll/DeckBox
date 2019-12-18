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
     * Start a new deck editing session, creating a new deck to edit into
     * @return the deck id to refer too later
     */
    fun startSession(imports: List<PokemonCard>? = null): Observable<Deck>

    /**
     * Change/Add the deck image for the given session ID
     */
    fun changeDeckImage(deckId: String, image: DeckImage): Observable<Unit>

    /**
     * Chagne the deck's collection only flag
     */
    fun changeCollectionOnly(deckId: String, collectionOnly: Boolean): Observable<Unit>

    /**
     * Change the name of an editing session
     */
    fun changeName(deckId: String, name: String): Observable<String>

    /**
     * Change the description of a session
     */
    fun changeDescription(deckId: String, description: String): Observable<String>

    /**
     * Add cards to session
     */
    fun addCards(deckId: String, cards: List<PokemonCard>): Observable<Unit>

    /**
     * Remove card from a session
     */
    fun removeCard(deckId: String, card: PokemonCard): Observable<Unit>

    companion object {

        /**
         * An id to pass to coordinate a new deck editing session but to indicate that the session hasn't been
         * created yet and needs to be started before making any edits
         */
        const val CREATE_DECK_ID = "create_new_deck"
    }
}
