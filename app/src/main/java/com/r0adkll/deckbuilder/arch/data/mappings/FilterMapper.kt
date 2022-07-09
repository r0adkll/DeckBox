package com.r0adkll.deckbuilder.arch.data.mappings

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import io.pokemontcg.ExperimentalPokemonApi
import io.pokemontcg.requests.CardQueryBuilder
import io.pokemontcg.requests.query.CardBuilder
import io.pokemontcg.requests.query.StringValue
import io.pokemontcg.requests.query.cardBuilder

object FilterMapper {

  @ExperimentalPokemonApi
  @Suppress("ComplexMethod")
  fun to(filter: Filter, additionalQueryBuilder: CardBuilder.() -> Unit): CardQueryBuilder {
    return CardQueryBuilder().apply {
      pageSize = filter.pageSize
      query = cardBuilder {
        additionalQueryBuilder()

        filter.superType?.let { superType ->
          supertype(superType)
        }

        if (filter.types.isNotEmpty()) {
          type(filter.types)
        }

        if (filter.subTypes.isNotEmpty()) {
          // TODO: Cleanup after API update
          subtypes(*filter.subTypes.toTypedArray())
        }

        if (filter.expansions.isNotEmpty()) {
          set {
            ids(filter.expansions.map { it.code })
          }
        }

        if (filter.rarity.isNotEmpty()) {
          rarity {
            isIn(filter.rarity.map { it.key })
          }
        }

        filter.retreatCost?.let { retreatCost ->
          retreatCost(retreatCost)
        }

        if (!filter.attackCost.isNullOrBlank()) {
          attacks {
            cost(filter.attackCost)
          }
        }

        if (!filter.attackDamage.isNullOrBlank()) {
          attacks {
            damage(filter.attackDamage)
          }
        }

        if (!filter.hp.isNullOrBlank()) {
          hp(filter.hp)
        }

        if (!filter.evolvesFrom.isNullOrBlank()) {
          evolvesFrom(filter.evolvesFrom)
        }

        if (filter.weaknesses.isNotEmpty()) {
          weaknesses {
            type(filter.weaknesses)
          }
        }

        if (filter.resistances.isNotEmpty()) {
          resistances {
            type(filter.resistances)
          }
        }
      }
    }
  }
}
