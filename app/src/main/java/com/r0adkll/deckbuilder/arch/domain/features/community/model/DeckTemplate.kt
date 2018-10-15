package com.r0adkll.deckbuilder.arch.domain.features.community.model

import android.os.Parcelable
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import kotlinx.android.parcel.Parcelize


/**
 * Template for creating a new deck
 *
 * @param deck the deck object that a new deck is going to be based off of
 * @param name the name of the template
 * @param description the description of the template (this could be author, or other metadata)
 */
@Parcelize
data class DeckTemplate(
        val deck: Deck,
        val name: String,
        val description: String
) : Parcelable