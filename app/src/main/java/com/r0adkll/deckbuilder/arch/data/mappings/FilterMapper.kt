package com.r0adkll.deckbuilder.arch.data.mappings

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import io.pokemontcg.requests.query.CardBuilder

object FilterMapper {

    @Suppress("ComplexMethod")
    fun to(filter: Filter): CardBuilder {
        val builder = CardBuilder()

        if (filter.superType != null) {
            builder.supertype(filter.superType)
        }

        if (filter.types.isNotEmpty()) {
            builder.type(filter.types)
        }

        if (filter.subTypes.isNotEmpty()) {
            builder.subtypes(filter.subTypes)
        }

//        if (filter.contains.isNotEmpty()) {
//            builder.contains = filter.contains.joinToString(separator = "|") { it.toLowerCase() }
//        }

        if (filter.expansions.isNotEmpty()) {
            builder.set {
                series(filter.expansions.map { it.series })
            }
        }

        if (filter.rarity.isNotEmpty()) {
            builder.rarity {
                isIn(filter.rarity.map { it.key })
            }
        }

        if (!filter.retreatCost.isNullOrBlank()) {
            builder.retreatCost(filter.retreatCost)
        }

        if (!filter.attackCost.isNullOrBlank()) {
            builder.attacks {
                cost(filter.attackCost)
            }
        }

        if (!filter.attackDamage.isNullOrBlank()) {
            builder.attacks {
                damage(filter.attackDamage)
            }
        }

        if (!filter.hp.isNullOrBlank()) {
            builder.hp(filter.hp)
        }

        if (!filter.evolvesFrom.isNullOrBlank()) {
            builder.evolvesFrom(filter.evolvesFrom)
        }

        if (filter.weaknesses.isNotEmpty()) {
            builder.weaknesses {
                type(filter.weaknesses)
            }
        }

        if (filter.resistances.isNotEmpty()) {
            builder.resistances {
                type(filter.resistances)
            }
        }

        return builder
    }
}
