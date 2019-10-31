package com.r0adkll.deckbuilder.arch.domain.features.community.model

import android.os.Parcelable
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import kotlinx.android.parcel.Parcelize

/**
 * Template for creating a new deck
 *
 * @param deck the deck object that a new deck is going to be based off of
 * @param name the name of the template
 * @param description the description of the template (this could be author, or other metadata)
 */
sealed class DeckTemplate(
        open val deck: Deck,
        open val name: String,
        open val description: String
) : Parcelable {

    @Parcelize
    data class TournamentDeckTemplate(
            override val deck: Deck,
            override val name: String,
            override val description: String,
            val author: String,
            val authorCountry: String,
            val tournament: Tournament,
            val deckInfo: List<DeckInfo>
    ): DeckTemplate(deck, name, description)

    @Parcelize
    data class ThemeDeckTemplate(
            override val deck: Deck,
            override val name: String,
            override val description: String,
            val expansion: Expansion
    ): DeckTemplate(deck, name, description)
}
