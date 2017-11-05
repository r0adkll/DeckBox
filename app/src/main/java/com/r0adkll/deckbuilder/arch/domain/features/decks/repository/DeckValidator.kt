package com.r0adkll.deckbuilder.arch.domain.features.decks.repository

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard


interface DeckValidator {

    fun validate(existing: List<PokemonCard>, cardToAdd: PokemonCard): Int?


    interface Rule {

        /**
         * Check if this rule passes or not
         * @return null for passing, otherwise it is an error message resource str
         */
        fun check(existing: List<PokemonCard>, card: PokemonCard): Int?
    }
}