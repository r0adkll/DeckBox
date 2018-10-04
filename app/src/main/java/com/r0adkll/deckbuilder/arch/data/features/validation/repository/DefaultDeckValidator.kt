package com.r0adkll.deckbuilder.arch.data.features.validation.repository


import com.r0adkll.deckbuilder.arch.data.remote.Remote
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
        val repository: CardRepository,
        val remote: Remote
) : DeckValidator {


    override fun validate(cards: List<PokemonCard>): Observable<Validation> {
        return repository.getExpansions()
                .onErrorReturnItem(emptyList())
                .map { expansions ->
                    val reprints = remote.reprints

                    // Validate for standard format
                    val standardLegal = cards.isNotEmpty() && cards.all { card ->
                        if (card.supertype == SuperType.ENERGY && card.subtype == SubType.BASIC) {
                            true
                        } else {
                            if (expansions.find { it.code == card.expansion?.code }?.standardLegal == true) {
                                true
                            } else if (reprints != null) {
                                val hash = card.reprintHash()
                                reprints.standardHashes.contains(hash)
                            } else {
                                false
                            }
                        }
                    }

                    val expandedLegal = cards.isNotEmpty() && cards.all { card ->
                        if (card.supertype == SuperType.ENERGY && card.subtype == SubType.BASIC) {
                            true
                        } else {
                            if (expansions.find { it.code == card.expansion?.code }?.expandedLegal == true) {
                                true
                            } else if (reprints != null) {
                                val hash = card.reprintHash()
                                reprints.expandedHashes.contains(hash)
                            } else {
                                false
                            }
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


    private fun PokemonCard.reprintHash(): Long {
        return (this.name.hashCode().toLong() * 31L) +
                (this.text?.hashCode()?.toLong() ?: 0L * 31L)
    }
}