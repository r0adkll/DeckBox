package com.r0adkll.deckbuilder.arch.data.features.collection.model


/**
 * A Collection card count index that represents how many of the card [cardId] they
 * have in their collection.
 *
 * @param cardId the id of the card
 * @param count the number of cards
 * @param set the expansion set the card belongs to
 * @param series the series that the set belongs to
 */
class CollectionCountEntity(
        val cardId: String = "",
        var count: Int = 0,
        val set: String = "",
        val series: String = ""
)