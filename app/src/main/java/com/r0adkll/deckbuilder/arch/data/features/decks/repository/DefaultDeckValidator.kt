package com.r0adkll.deckbuilder.arch.data.features.decks.repository


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckValidator
import javax.inject.Inject


class DefaultDeckValidator @Inject constructor(
        val rules: Set<@JvmSuppressWildcards DeckValidator.Rule>
) : DeckValidator {

    override fun validate(existing: List<PokemonCard>, cardToAdd: PokemonCard): Int? {
        rules.forEach {
            val result = it.check(existing, cardToAdd)
            if (result != null) {
                return result
            }
        }
        return null
    }
}