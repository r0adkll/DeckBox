package com.r0adkll.deckbuilder.arch.data.features.validation.model


import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Rule


class PrismStarRule : Rule {

    private val MAXCOUNT = 1


    override fun check(cards: List<PokemonCard>): Int? {
        val groups = cards.filter { it.name.contains("◇") }.groupBy { it.name }
        val invalidGroups = groups.values.find { it.size > MAXCOUNT }

        return if (invalidGroups == null) {
            null
        } else {
            R.string.validation_rule_prism_star
        }
    }
}