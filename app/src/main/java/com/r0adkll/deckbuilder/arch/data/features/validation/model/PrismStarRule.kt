package com.r0adkll.deckbuilder.arch.data.features.validation.model

import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Rule

class PrismStarRule : Rule {

    override fun check(cards: List<PokemonCard>): Int? {
        val groups = cards.filter { it.name.contains(CardUtils.PRISM_SYMBOL) }.groupBy { it.name }
        val invalidGroups = groups.values.find { it.size > MAX_COUNT }

        return if (invalidGroups == null) {
            null
        } else {
            R.string.validation_rule_prism_star
        }
    }

    companion object {
        private const val MAX_COUNT = 1
    }
}
