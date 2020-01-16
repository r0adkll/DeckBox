package com.r0adkll.deckbuilder.arch.data.features.validation.repository

import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.expansions.repository.ExpansionRepository
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Rule
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Validation
import com.r0adkll.deckbuilder.arch.domain.features.validation.repository.DeckValidator
import com.r0adkll.deckbuilder.util.extensions.sortableNumber
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import javax.inject.Inject

@SuppressLint("DefaultLocale")
class DefaultDeckValidator @Inject constructor(
    val rules: Set<@JvmSuppressWildcards Rule>,
    val expansionRepository: ExpansionRepository,
    val editRepository: EditRepository,
    val remote: Remote
) : DeckValidator {

    override fun validate(deckId: String): Observable<Validation> {
        return editRepository.observeSession(deckId)
            .flatMap { validate(it.cards) }
    }

    override fun validate(cards: List<PokemonCard>): Observable<Validation> {
        return expansionRepository.getExpansions()
            .onErrorReturnItem(emptyList())
            .map { expansions ->
                // Validate formats
                val standardLegal = checkStandardLegal(expansions, cards)
                val expandedLegal = checkExpandedLegal(expansions, cards)

                // Validate rules
                val ruleResults = rules
                    .mapNotNull { it.check(cards) }
                    .map { it }
                    .toMutableList()

                Validation(standardLegal, expandedLegal, ruleResults)
            }
    }

    private fun checkStandardLegal(expansions: List<Expansion>, cards: List<PokemonCard>): Boolean {
        val reprints = remote.reprints
        val banList = remote.banList
        val legalOverrides = remote.legalOverrides
        val legalPromoOverride = legalOverrides?.promos
        return cards.isNotEmpty() && cards.all { card ->
            if (card.supertype == SuperType.ENERGY && card.subtype == SubType.BASIC) {
                true
            } else {
                val singlesOverride = legalOverrides?.singles?.find { card.id.equals(it.id, true) }
                val cardsExpansionCode = singlesOverride?.sourceSetCode ?: card.expansion?.code
                val cardId = singlesOverride?.sourceId ?: card.id
                when {
                    legalPromoOverride != null && cardsExpansionCode == legalPromoOverride.setCode -> {
                        card.sortableNumber >= legalOverrides.promos.startNumber
                    }
                    expansions.find { it.code == cardsExpansionCode }?.standardLegal == true -> {
                        true
                    }
                    reprints != null -> {
                        val hash = card.reprintHash()
                        reprints.standardHashes.contains(hash)
                    }
                    else -> false
                } && banList?.standard?.contains(cardId.toLowerCase()) != true
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun checkExpandedLegal(expansions: List<Expansion>, cards: List<PokemonCard>): Boolean {
        val reprints = remote.reprints
        val banList = remote.banList
        val legalOverrides = remote.legalOverrides
        val legalPromoOverride = legalOverrides?.expandedPromos
        return cards.isNotEmpty() && cards.all { card ->
            if (card.supertype == SuperType.ENERGY && card.subtype == SubType.BASIC) {
                true
            } else {
                val singlesOverride = legalOverrides?.singles?.find { card.id.equals(it.id, true) }
                val cardId = singlesOverride?.sourceId ?: card.id
                val cardsExpansionCode = singlesOverride?.sourceSetCode ?: card.expansion?.code
                when {
                    legalPromoOverride != null && cardsExpansionCode == legalPromoOverride.setCode -> {
                        card.sortableNumber >= legalOverrides.expandedPromos.startNumber
                    }
                    expansions.find { it.code == cardsExpansionCode }?.expandedLegal == true -> {
                        true
                    }
                    reprints != null -> {
                        val hash = card.reprintHash()
                        reprints.expandedHashes.contains(hash)
                    }
                    else -> false
                } && banList?.expanded?.contains(cardId.toLowerCase()) != true
            }
        }
    }

    @Suppress("MagicNumber")
    private fun PokemonCard.reprintHash(): Long {
        return (this.name.hashCode().toLong() * 31L) +
            (this.text?.hashCode()?.toLong() ?: 0L * 31L)
    }
}
