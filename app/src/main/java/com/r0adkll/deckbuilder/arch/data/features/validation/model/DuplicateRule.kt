package com.r0adkll.deckbuilder.arch.data.features.validation.model

import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Rule
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType

class DuplicateRule : Rule {

    override fun check(cards: List<PokemonCard>): Int? {
        val groups = cards.groupBy { it.name }
        val invalidGroups = groups.values.find {
            it.size > MAX_COUNT &&
                !(it.first().supertype == SuperType.ENERGY && it.first().subtype == SubType.BASIC)
        }

        return if (invalidGroups == null) {
            null
        } else {
            R.string.validation_rule_four_of_a_kind
        }
    }

    companion object {
        private const val MAX_COUNT = 4
    }
}
