package com.r0adkll.deckbuilder.arch.data.features.decks.rules

import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckValidator
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import timber.log.Timber


class DuplicateRule : DeckValidator.Rule {

    private val MAXCOUNT = 4

    override fun check(existing: List<PokemonCard>, card: PokemonCard): Int? {
        Timber.i("Duplicate Check(${card.id})")
        if (card.supertype == SuperType.ENERGY && card.subtype == SubType.BASIC) {
            // We ignore basic energy as you can have as much as you like
            return null
        }

        return if (existing.count { it.name.equals(card.name, true) } < MAXCOUNT) {
            null
        } else {
            R.string.validation_rule_four_of_a_kind
        }
    }
}