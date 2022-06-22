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
        var filterQuery = startQuery(query, filter)

        filterQuery = applyTypes(filter, filterQuery)
        filterQuery = applySupertype(filter, filterQuery)
        filterQuery = applySubtypes(filter, filterQuery)

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
        filterQuery = applyRetreatCost(filter, filterQuery)
        filterQuery = applyHP(filter, filterQuery)

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

    private fun startQuery(query: String, filter: Filter?): WhereAndOr<String> {
        val statement = Query.select("cards")
        return if (query.isNotBlank()) {
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
    }

    private fun applyTypes(filter: Filter?, query: WhereAndOr<String>): WhereAndOr<String> {
        var filterQuery = query
        filter?.types?.forEach { t ->
            val type = t.compact()
            filterQuery = filterQuery.and("types" like "%$type%")
        }
        return filterQuery
    }

    private fun applySupertype(filter: Filter?, query: WhereAndOr<String>): WhereAndOr<String> {
        return filter?.superType?.let {
            query.and("superType" eq it.displayName)
        } ?: query
    }

    private fun applySubtypes(filter: Filter?, query: WhereAndOr<String>): WhereAndOr<String> {
        return if (filter?.subTypes?.isNotEmpty() == true) {
            when {
                filter.subTypes.size == 1 -> query.and("subType" eq filter.subTypes[0])
                filter.subTypes.size > 1 -> applyMultipleSubtypes(filter, query)
                else -> query
            }
        } else {
            query
        }
    }

    private fun applyMultipleSubtypes(filter: Filter, query: WhereAndOr<String>): WhereAndOr<String> {
        var condition = ("subType" eq filter.subTypes[0]) or
            ("subType" eq filter.subTypes[1])
        if (filter.subTypes.size > 2) {
            (2 until filter.subTypes.size).forEach {
                val subType = filter.subTypes[it]
                condition = condition or ("subType" eq subType)
            }
        }
        return query.and(condition)
    }

    private fun applyRetreatCost(filter: Filter?, query: WhereAndOr<String>): WhereAndOr<String> {
        return filter?.retreatCost?.let {
            val value = FilterSpec.Spec.ValueRangeSpec.parseValue(it)
            when (value.modifier) {
                Item.ValueRange.Modifier.GREATER_THAN -> query.and("retreatCost" gt value.value)
                Item.ValueRange.Modifier.GREATER_THAN_EQUALS -> query.and("retreatCost" gte value.value)
                Item.ValueRange.Modifier.LESS_THAN -> query.and("retreatCost" lt value.value)
                Item.ValueRange.Modifier.LESS_THAN_EQUALS -> query.and("retreatCost" lte value.value)
                Item.ValueRange.Modifier.NONE -> query.and("retreatCost" eq value.value)
            }
        } ?: query
    }

    private fun applyHP(filter: Filter?, query: WhereAndOr<String>): WhereAndOr<String> {
        return filter?.hp?.let {
            val value = FilterSpec.Spec.ValueRangeSpec.parseValue(it)
            when (value.modifier) {
                Item.ValueRange.Modifier.GREATER_THAN -> query.and("hp" gt value.value)
                Item.ValueRange.Modifier.GREATER_THAN_EQUALS -> query.and("hp" gte value.value)
                Item.ValueRange.Modifier.LESS_THAN -> query.and("hp" lt value.value)
                Item.ValueRange.Modifier.LESS_THAN_EQUALS -> query.and("hp" lte value.value)
                Item.ValueRange.Modifier.NONE -> query.and("hp" eq value.value)
            }
        } ?: query
    }
}
