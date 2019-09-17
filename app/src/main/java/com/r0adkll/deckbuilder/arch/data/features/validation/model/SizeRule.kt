package com.r0adkll.deckbuilder.arch.data.features.validation.model

import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Rule

class SizeRule : Rule {

    override fun check(cards: List<PokemonCard>): Int? {
        return if (cards.size == MAX_SIZE) {
            null
        } else {
            R.string.validation_rule_max_size
        }
    }

    companion object {
        private const val MAX_SIZE = 60
    }
}
