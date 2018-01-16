package com.r0adkll.deckbuilder.arch.data.features.validation.model


import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Rule


class SizeRule : Rule {

    private val MAXSIZE = 60

    override fun check(cards: List<PokemonCard>): Int? {
        return if (cards.size <= MAXSIZE) {
            null
        } else {
            R.string.validation_rule_max_size
        }
    }
}