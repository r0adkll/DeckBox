package com.r0adkll.deckbuilder.arch.domain.features.validation.model


import androidx.annotation.StringRes
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard


interface Rule {

    /**
     * Check if the list of cards, the current representation of a deck, is valid for this implementation of
     * the rule
     *
     * @param cards the list of cards in a deck to check/validate
     * @return the error message, as a [StringRes], or null if cards are valid for this rule
     */
    @StringRes fun check(cards: List<PokemonCard>): Int?
}