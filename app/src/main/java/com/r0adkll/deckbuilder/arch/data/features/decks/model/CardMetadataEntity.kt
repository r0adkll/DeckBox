package com.r0adkll.deckbuilder.arch.data.features.decks.model

/**
 * Barebones card reference to avoid store the entirety of the card in FireStore
 *
 * @param id the pokemon card id
 * @param superType the [io.pokemontcg.model.SuperType] of the pokemon card
 * @param imageUrl the small card image url
 * @param imageUrl the HiRes card image url
 * @param count the number of instances of this card in the deck
 * @param previewSet the set that this preview is from.
 */
class CardMetadataEntity(
    val id: String = "",
    val superType: String = "",
    val imageUrl: String = "",
    val imageUrlHiRes: String = "",
    val count: Int = 0,
    val previewSet: String? = null
)
