package com.r0adkll.deckbuilder.arch.domain.features.decks.repository

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import io.reactivex.Observable

interface DeckRepository {

    fun getDeck(id: String): Observable<Deck>

    fun getDecks(): Observable<List<Deck>>

    fun persistDeck(
        id: String?,
        cards: List<PokemonCard>,
        name: String,
        description: String?,
        image: DeckImage?,
        collectionOnly: Boolean
    ): Observable<Deck>

    fun duplicateDeck(deck: Deck): Observable<Unit>

    fun deleteDeck(deck: Deck): Observable<Unit>
}
