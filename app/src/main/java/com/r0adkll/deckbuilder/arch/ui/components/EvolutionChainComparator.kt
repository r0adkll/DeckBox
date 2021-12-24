@file:Suppress("MagicNumber")

package com.r0adkll.deckbuilder.arch.ui.components

import com.r0adkll.deckbuilder.arch.domain.SubTypes
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import io.pokemontcg.model.SuperType

private typealias SubType = String

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
        lhs.nationalPokedexNumber?.compareTo(
          rhs.nationalPokedexNumber
            ?: Int.MAX_VALUE
        ) ?: 0
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
    return lhs.subtype.weight(lhs.supertype).compareTo(rhs.subtype.weight(rhs.supertype))
  }

  private fun SuperType.weight(): Int = when (this) {
    SuperType.POKEMON -> 0
    SuperType.TRAINER -> 1
    else -> 2
  }

  private fun SubType.weight(superType: SuperType): Int = when (superType) {
    SuperType.ENERGY -> energyWeight()
    SuperType.TRAINER -> trainerWeight()
    else -> 0
  }

  private fun SubType.energyWeight(): Int = when (this) {
    SubTypes.BASIC -> 1
    SubTypes.SPECIAL -> 0
    else -> 0
  }

  private fun SubType.trainerWeight(): Int = when (this) {
    SubTypes.ITEM -> 0
    SubTypes.SUPPORTER -> 1
    SubTypes.STADIUM -> 2
    SubTypes.POKEMON_TOOL -> 3
    else -> 4
  }
}
