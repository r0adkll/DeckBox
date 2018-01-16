package com.r0adkll.deckbuilder.arch.data.features.validation.repository


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Rule
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Validation
import com.r0adkll.deckbuilder.arch.domain.features.validation.repository.DeckValidator
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import javax.inject.Inject


class DefaultDeckValidator @Inject constructor(
        val rules: Set<@JvmSuppressWildcards Rule>,
        val repository: CardRepository
) : DeckValidator {


    override fun validate(cards: List<PokemonCard>): Observable<Validation> {
        return repository.getExpansions()
                .onErrorReturnItem(emptyList())
                .map { expansions ->

                    // Validate for standard format
                    // TODO: Find a way to account for reprints
                    val standardLegal = cards.isNotEmpty() && cards.all { card ->
                        if (card.supertype == SuperType.ENERGY && card.subtype == SubType.BASIC) {
                            true
                        } else {
                            expansions.find { it.code == card.expansion?.code }?.standardLegal ?: false
                        }
                    }

                    // TODO: Find a way to account for reprints
                    val expandedLegal = cards.isNotEmpty() && cards.all { card ->
                        if (card.supertype == SuperType.ENERGY && card.subtype == SubType.BASIC) {
                            true
                        } else {
                            expansions.find { it.code == card.expansion?.code }?.expandedLegal ?: false
                        }
                    }

                    // Check all the rules
                    val ruleResults = rules
                            .map { it.check(cards) }
                            .filter { it != null }
                            .map { it!! }

                    Validation(standardLegal, expandedLegal, ruleResults)
                }
    }
}