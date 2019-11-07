package com.r0adkll.deckbuilder.arch.ui.components

import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard

/**
 * Helper class to group the adding and removing relays for editing a deck in the UI
 * so we don't have to pass the relays individually
 */
class EditCardIntentions(
    val addCardClicks: Relay<List<PokemonCard>> = PublishRelay.create(),
    val removeCardClicks: Relay<PokemonCard> = PublishRelay.create()
)
