package com.r0adkll.deckbuilder.arch.domain.features.editing.model

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage

/**
 * Edit directive class to create deck edits on an existing, or new deck
 * @param deckId the id of the deck to edit
 */
sealed class Edit {

    class Name(val name: String) : Edit()
    class Image(val image: DeckImage) : Edit()
    class Description(val description: String) : Edit()
    class CollectionOnly(val collectionOnly: Boolean) : Edit()
    class AddCards(val cards: List<PokemonCard>) : Edit() {
        constructor(vararg cards: PokemonCard) : this(cards.toList())
    }
    class RemoveCard(val card: PokemonCard) : Edit()
}
