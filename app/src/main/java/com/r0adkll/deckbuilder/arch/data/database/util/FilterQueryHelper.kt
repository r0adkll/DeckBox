package com.r0adkll.deckbuilder.arch.data.database.util

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.SearchField
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterSpec
import com.r0adkll.deckbuilder.arch.ui.features.filter.adapter.Item
import com.r0adkll.deckbuilder.util.compact
import timber.log.Timber


object FilterQueryHelper {

    /**
     * Create a [SupportSQLiteQuery] from a [Filter] and a query to use with Room's RawQuery support
     * @param query the adjusted search query
     * @param filter the search filter
     */
    fun createQuery(query: String, filter: Filter?): SupportSQLiteQuery {
        val statement = Query.select("cards")

        // Adjust statement for search field
        var filterQuery = if (query.isNotBlank()) {
            when (filter?.field ?: SearchField.NAME) {
                SearchField.NAME -> statement.where("name" like "%$query%")
                SearchField.TEXT -> statement.where("text" like "%$query%")
                SearchField.ABILITY_NAME -> statement.where("ability_name" like "%$query%")
                SearchField.ABILITY_TEXT -> statement.where("ability_text" like "%$query%")
                SearchField.ATTACK_TEXT -> {
                    statement.join("attacks").on("attacks.cardId" eq "cards.id")
                            .where("attacks.text" like "%$query%")
                }
                SearchField.ATTACK_NAME -> {
                    statement.join("attacks").on("attacks.cardId" eq "cards.id")
                            .where("attacks.name" like "%$query%")
                }
            }
        } else {
            // This is a nothing statement that will always result in true for every row
            statement.where("name".notNull())
        }

        // Adjust query for types
        filter?.types?.forEach { t ->
            val type = t.compact()
            filterQuery = filterQuery.and("types" like "%$type%")
        }

        // Adjust query for supertype
        filter?.superType?.let {
            filterQuery = filterQuery.and("superType" eq it.displayName)
        }

        // Adjust query for subtypes
        if (filter?.subTypes?.isNotEmpty() == true) {
            if (filter.subTypes.size == 1) {
                filterQuery = filterQuery.and("subType" eq filter.subTypes[0].displayName)
            } else if (filter.subTypes.size > 1) {
                var condition = ("subType" eq filter.subTypes[0].displayName) or ("subType" eq filter.subTypes[1].displayName)
                if (filter.subTypes.size > 2) {
                    (2 until filter.subTypes.size).forEach {
                        val subType = filter.subTypes[it]
                        condition = condition or ("subType" eq subType.displayName)
                    }
                }

                filterQuery = filterQuery.and(condition)
            }
        }

        // Adjust query for evolvesFrom
        filter?.evolvesFrom?.let {
            filterQuery = filterQuery.and("evolvesFrom" eq it)
        }

        // Adjust query for contains
        filter?.contains?.forEach {
            if (it.equals("Ability", true)) {
                filterQuery = filterQuery.and("ability".notNull())
            } // else ignore
        }

        // Adjust query for expansions
        if (filter?.expansions?.isNotEmpty() == true) {
            val setCodes = filter.expansions.map { it.code }
            filterQuery = filterQuery.and("setCode".`in`(setCodes))
        }

        // Adjust query for rarities
        if (filter?.rarity?.isNotEmpty() == true) {
            val rarities = filter.rarity.map { it.key }
            filterQuery = filterQuery.and("rarity".`in`(rarities))
        }

        // Adjust query for retreatCost
        filter?.retreatCost?.let {
            val value = FilterSpec.Spec.ValueRangeSpec.parseValue(it)
            filterQuery = when(value.modifier) {
                Item.ValueRange.Modifier.GREATER_THAN -> filterQuery.and("retreatCost" gt value.value)
                Item.ValueRange.Modifier.GREATER_THAN_EQUALS -> filterQuery.and("retreatCost" gte value.value)
                Item.ValueRange.Modifier.LESS_THAN -> filterQuery.and("retreatCost" lt value.value)
                Item.ValueRange.Modifier.LESS_THAN_EQUALS -> filterQuery.and("retreatCost" lte value.value)
                Item.ValueRange.Modifier.NONE -> filterQuery.and("retreatCost" eq value.value)
            }
        }

        // Adjust query for attackCost
        filter?.hp?.let {
            val value = FilterSpec.Spec.ValueRangeSpec.parseValue(it)
            filterQuery = when(value.modifier) {
                Item.ValueRange.Modifier.GREATER_THAN -> filterQuery.and("hp" gt value.value)
                Item.ValueRange.Modifier.GREATER_THAN_EQUALS -> filterQuery.and("hp" gte value.value)
                Item.ValueRange.Modifier.LESS_THAN -> filterQuery.and("hp" lt value.value)
                Item.ValueRange.Modifier.LESS_THAN_EQUALS -> filterQuery.and("hp" lte value.value)
                Item.ValueRange.Modifier.NONE -> filterQuery.and("hp" eq value.value)
            }
        }

        // Adjust for weaknesses
        filter?.weaknesses?.forEach { t ->
            val type = t.compact()
            filterQuery = filterQuery.and("weaknesses" like "%$type%")
        }

        // Adjust for weaknesses
        filter?.resistances?.forEach { t ->
            val type = t.compact()
            filterQuery = filterQuery.and("resistances" like "%$type%")
        }

        val compiledSql = filterQuery.get()
        Timber.i("SQL: $compiledSql")
        return SimpleSQLiteQuery(compiledSql)
    }
}