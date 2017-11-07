package com.r0adkll.deckbuilder.arch.domain.features.decks.repository

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Validation
import io.reactivex.Observable


interface DeckValidator {

    fun validate(existing: List<PokemonCard>, cardToAdd: PokemonCard): Int?
    fun validate(cards: List<PokemonCard>): Observable<Validation>


    interface Rule {

        /**
         * Check if this rule passes or not
         * @return null for passing, otherwise it is an error message resource str
         */
        fun check(existing: List<PokemonCard>, card: PokemonCard): Int?
    }
}