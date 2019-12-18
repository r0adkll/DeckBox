package com.r0adkll.deckbuilder.arch.data.features.editing.source

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import io.reactivex.Observable

interface EditSource {

    fun startSession(imports: List<PokemonCard>?): Observable<Deck>
    fun changeName(deckId: String, name: String): Observable<String>
    fun changeDescription(deckId: String, description: String): Observable<String>
    fun changeDeckImage(deckId: String, image: DeckImage): Observable<Unit>
    fun changeCollectionOnly(deckId: String, collectionOnly: Boolean): Observable<Unit>
    fun addCards(deckId: String, cards: List<PokemonCard>): Observable<Unit>
    fun removeCard(deckId: String, card: PokemonCard): Observable<Unit>
}
