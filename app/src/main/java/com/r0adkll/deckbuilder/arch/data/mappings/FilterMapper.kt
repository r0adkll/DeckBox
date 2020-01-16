package com.r0adkll.deckbuilder.arch.data.mappings

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import io.pokemontcg.requests.CardQueryBuilder

object FilterMapper {

    @Suppress("ComplexMethod")
    fun to(filter: Filter): CardQueryBuilder {
        val builder = CardQueryBuilder()

        if (filter.superType != null) {
            builder.supertype = filter.superType.displayName
        }

        if (filter.types.isNotEmpty()) {
            builder.types = filter.types.joinToString(separator = "|")
        }

        if (filter.subTypes.isNotEmpty()) {
            builder.subtype = filter.subTypes.joinToString(separator = "|")
        }

        if (filter.contains.isNotEmpty()) {
            builder.contains = filter.contains.joinToString(separator = "|") { it.toLowerCase() }
        }

        if (filter.expansions.isNotEmpty()) {
            builder.setCode = filter.expansions.joinToString("|") { it.code }
        }

        if (filter.rarity.isNotEmpty()) {
            builder.rarity = filter.rarity.joinToString("|") { it.key }
        }

        if (!filter.retreatCost.isNullOrBlank()) {
            builder.retreatCost = filter.retreatCost
        }

        if (!filter.attackCost.isNullOrBlank()) {
            builder.attackCost = filter.attackCost
        }

        if (!filter.attackDamage.isNullOrBlank()) {
            builder.attackDamage = filter.attackDamage
        }

        if (!filter.hp.isNullOrBlank()) {
            builder.hp = filter.hp
        }

        if (!filter.evolvesFrom.isNullOrBlank()) {
            builder.evolvesFrom = filter.evolvesFrom
        }

        if (filter.weaknesses.isNotEmpty()) {
            builder.weaknesses = filter.weaknesses.joinToString("|") { it.displayName }
        }

        if (filter.resistances.isNotEmpty()) {
            builder.resistances = filter.resistances.joinToString("|") { it.displayName }
        }

        builder.pageSize = filter.pageSize

        return builder
    }
}
