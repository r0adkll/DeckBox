package com.r0adkll.deckbuilder.arch.data.features.decks.repository


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Validation
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckValidator
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import javax.inject.Inject


class DefaultDeckValidator @Inject constructor(
        val rules: Set<@JvmSuppressWildcards DeckValidator.Rule>,
        val repository: CardRepository
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


    override fun validate(cards: List<PokemonCard>): Observable<Validation> {
        return repository.getExpansions()
                .onErrorReturnItem(emptyList())
                .map { expansions ->
                    val standardLegal = cards.all { card ->
                        if (card.supertype == SuperType.ENERGY && card.subtype == SubType.BASIC) {
                            true
                        } else {
                            expansions.find { it.code == card.expansion?.code }?.standardLegal ?: false
                        }
                    }

                    val expandedLegal = cards.all { card ->
                        if (card.supertype == SuperType.ENERGY && card.subtype == SubType.BASIC) {
                            true
                        } else {
                            expansions.find { it.code == card.expansion?.code }?.expandedLegal ?: false
                        }
                    }

                    Validation(standardLegal, expandedLegal)
                }
    }
}