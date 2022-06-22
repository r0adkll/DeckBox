@file:Suppress("MagicNumber")

package com.r0adkll.deckbuilder.arch.ui.components

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import io.pokemontcg.model.SuperType

object EvolutionChainComparator : Comparator<EvolutionChain> {

    override fun compare(lhs: EvolutionChain, rhs: EvolutionChain): Int {
        // 0 - Sort by node size1
        val r0 = rhs.size.compareTo(lhs.size)
        return if (r0 == 0) {
            // 1 - Sort by SuperType
            val lhsCard = lhs.first()?.cards?.firstOrNull()?.card
            val rhsCard = rhs.first()?.cards?.firstOrNull()?.card
            if (lhsCard != null && rhsCard != null) {
                val r1 = compareSupertype(lhsCard, rhsCard)
                if (r1 == 0) {
                    resolveSupertypes(lhsCard, rhsCard)
                } else {
                    r1
                }
            } else {
                r0
            }
        } else {
            r0
        }
    }

    private fun resolveSupertypes(lhs: PokemonCard, rhs: PokemonCard): Int {
        // 2 - Based on SuperType, sort by type characteristics
        return when (lhs.supertype) {
            SuperType.POKEMON -> {
                // 2a - Pokemon - Sort National Dex Number
                lhs.nationalPokedexNumbers?.get(0)?.compareTo(rhs.nationalPokedexNumbers?.get(0)
                    ?: Int.MAX_VALUE) ?: 0
            }
            SuperType.TRAINER -> {
                // 2b - Trainer -> Item > Supporter > Stadium > Tool
                compareSubtype(lhs, rhs)
            }
            SuperType.ENERGY -> {
                // 2c - Energy - Sort by subtype (Special > Basic)
                compareSubtype(lhs, rhs)
            }
            else -> 0
        }
    }

    private fun compareSupertype(lhs: PokemonCard, rhs: PokemonCard): Int {
        return lhs.supertype.weight().compareTo(rhs.supertype.weight())
    }

    private fun compareSubtype(lhs: PokemonCard, rhs: PokemonCard): Int {
        return lhs.subtypes!![0].weight(lhs.supertype).compareTo(rhs.subtypes!![0].weight(rhs.supertype))
    }

    private fun SuperType.weight(): Int = when (this) {
        SuperType.POKEMON -> 0
        SuperType.TRAINER -> 1
        else -> 2
    }

    private fun String.weight(superType: SuperType): Int = when (superType) {
        SuperType.ENERGY -> energyWeight()
        SuperType.TRAINER -> trainerWeight()
        else -> 0
    }

    private fun String.energyWeight(): Int = when (this) {
        "basic" -> 1
        "special" -> 0
        else -> 0
    }

    private fun String.trainerWeight(): Int = when (this) {
        "item" -> 0
        "supporter" -> 1
        "stadium" -> 2
        "tool" -> 3
        else -> 4
    }
}
