package com.r0adkll.deckbuilder.arch.data.features.validation.model

import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Rule
import io.pokemontcg.model.SuperType

class BasicRule : Rule {

    override fun check(cards: List<PokemonCard>): Int? {
        val basicCard = cards.find {
            it.supertype == SuperType.POKEMON && it.evolvesFrom == null
        }

        return if (basicCard != null) {
            null
        } else {
            R.string.validation_rule_basic
        }
    }
}
