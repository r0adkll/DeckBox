package com.r0adkll.deckbuilder.arch.data.features.decks.rules

import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckValidator
import timber.log.Timber


class SizeRule : DeckValidator.Rule {

    private val MAXSIZE = 60

    override fun check(existing: List<PokemonCard>, card: PokemonCard): Int? {
        Timber.i("Size Rule check ${existing.size}")
        return if (existing.size < MAXSIZE) {
            null
        } else {
            R.string.validation_rule_max_size
        }
    }
}