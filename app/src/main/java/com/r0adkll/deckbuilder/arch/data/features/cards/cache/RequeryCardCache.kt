package com.r0adkll.deckbuilder.arch.data.features.cards.cache


import com.r0adkll.deckbuilder.arch.data.remote.Remote
import com.r0adkll.deckbuilder.arch.data.database.entities.AttackEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.CardEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.ICardEntity
import com.r0adkll.deckbuilder.arch.data.database.mapping.EntityMapper
import com.r0adkll.deckbuilder.arch.data.features.expansions.ExpansionDataSource
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.SearchField
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterSpec
import com.r0adkll.deckbuilder.arch.ui.features.filter.adapter.Item.ValueRange.Modifier.*
import com.r0adkll.deckbuilder.util.compact
import io.pokemontcg.model.Card
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.requery.Persistable
import io.requery.reactivex.KotlinReactiveEntityStore
import javax.inject.Inject


class RequeryCardCache @Inject constructor(
        val db: KotlinReactiveEntityStore<Persistable>,
        val cache: ExpansionDataSource,
        val remote: Remote
) : CardCache {

    override fun putCards(cards: List<Card>) {
        val entities = EntityMapper.to(cards)
        db.toBlocking().insert(entities)
    }


    override fun findCards(ids: List<String>): Observable<List<PokemonCard>> {
        val query = db.select(ICardEntity::class)
                .where(CardEntity.CARD_ID.`in`(ids))
                .get()
                .observable()
                .toList()
                .toObservable()

        return Observable.combineLatest(query, cache.getExpansions(), BiFunction { entities, expansions ->
            EntityMapper.from(expansions, entities)
        })
    }


    override fun findCards(query: String, filter: Filter?): Observable<List<PokemonCard>> {
        val adjustedQuery = remote.searchProxies?.apply(query) ?: query
        val statement = db.select(ICardEntity::class)

        // Adjust statement for search field
        var filterQuery = if (query.isNotBlank()) {
            when (filter?.field ?: SearchField.NAME) {
                SearchField.NAME -> statement.where(CardEntity.NAME.like("%$adjustedQuery%"))
                SearchField.TEXT -> statement.where(CardEntity.TEXT.like("%$adjustedQuery%"))
                SearchField.ABILITY_NAME -> statement.where(CardEntity.ABILITY_NAME.like("%$adjustedQuery%"))
                SearchField.ABILITY_TEXT -> statement.where(CardEntity.ABILITY_TEXT.like("%$adjustedQuery%"))
                SearchField.ATTACK_TEXT -> {
                    statement.join(AttackEntity::class).on(AttackEntity.CARD_ID.eq(CardEntity.ID))
                            .where(AttackEntity.TEXT.like("%$adjustedQuery%"))
                }
                SearchField.ATTACK_NAME -> {
                    statement.join(AttackEntity::class).on(AttackEntity.CARD_ID.eq(CardEntity.ID))
                            .where(AttackEntity.NAME.like("%$adjustedQuery%"))
                }
            }
        } else {
            // This is a nothing statement that will always result in true for every row
            statement.where(CardEntity.NAME.notNull())
        }

        // Adjust query for types
        filter?.types?.forEach { t ->
            val type = t.compact()
            filterQuery = filterQuery.and(CardEntity.TYPES.like("%$type%"))
        }

        // Adjust query for supertype
        filter?.superType?.let {
            filterQuery = filterQuery.and(CardEntity.SUPER_TYPE.eq(it.displayName))
        }

        // Adjust query for subtypes
        if (filter?.subTypes?.isNotEmpty() == true) {
            if (filter.subTypes.size == 1) {
                filterQuery = filterQuery.and(CardEntity.SUB_TYPE.eq(filter.subTypes[0].displayName))
            } else if (filter.subTypes.size > 1) {
                var condition = CardEntity.SUB_TYPE.eq(filter.subTypes[0].displayName)
                        .or(CardEntity.SUB_TYPE.eq(filter.subTypes[1].displayName))
                if (filter.subTypes.size > 2) {
                    (2 until filter.subTypes.size).forEach {
                        val subType = filter.subTypes[it]
                        condition = condition.or(CardEntity.SUB_TYPE.eq(subType.displayName))
                    }
                }

                filterQuery = filterQuery.and(condition)
            }
        }

        // Adjust query for contains
        filter?.contains?.forEach {
            if (it.equals("AbilityEntity", true)) {
                filterQuery = filterQuery.and(CardEntity.ABILITY_NAME.notNull())
            } // else ignore
        }

        // Adjust query for expansions
        if (filter?.expansions?.isNotEmpty() == true) {
            val setCodes = filter.expansions.map { it.code }
            filterQuery = filterQuery.and(CardEntity.SET_CODE.`in`(setCodes))
        }

        // Adjust query for rarities
        if (filter?.rarity?.isNotEmpty() == true) {
            val rarities = filter.rarity.map { it.key }
            filterQuery = filterQuery.and(CardEntity.RARITY.`in`(rarities))
        }

        // Adjust query for retreatCost
        filter?.retreatCost?.let {
            val value = FilterSpec.Spec.ValueRangeSpec.parseValue(it)
            filterQuery = when(value.modifier) {
                GREATER_THAN -> filterQuery.and(CardEntity.RETREAT_COST.gt(value.value))
                GREATER_THAN_EQUALS -> filterQuery.and(CardEntity.RETREAT_COST.gte(value.value))
                LESS_THAN -> filterQuery.and(CardEntity.RETREAT_COST.lt(value.value))
                LESS_THAN_EQUALS -> filterQuery.and(CardEntity.RETREAT_COST.lte(value.value))
                NONE -> filterQuery.and(CardEntity.RETREAT_COST.eq(value.value))
            }
        }

        // Adjust query for attackCost
        filter?.hp?.let {
            val value = FilterSpec.Spec.ValueRangeSpec.parseValue(it)
            filterQuery = when(value.modifier) {
                GREATER_THAN -> filterQuery.and(CardEntity.HP.gt(value.value))
                GREATER_THAN_EQUALS -> filterQuery.and(CardEntity.HP.gte(value.value))
                LESS_THAN -> filterQuery.and(CardEntity.HP.lt(value.value))
                LESS_THAN_EQUALS -> filterQuery.and(CardEntity.HP.lte(value.value))
                NONE -> filterQuery.and(CardEntity.HP.eq(value.value))
            }
        }

        // Adjust for weaknesses
        filter?.weaknesses?.forEach { t ->
            val type = t.compact()
            filterQuery = filterQuery.and(CardEntity.WEAKNESSES.like("%$type%"))
        }

        // Adjust for weaknesses
        filter?.resistances?.forEach { t ->
            val type = t.compact()
            filterQuery = filterQuery.and(CardEntity.RESISTANCES.like("%$type%"))
        }

        return Observable.combineLatest(filterQuery.get().observable().toList().toObservable(), cache.getExpansions(), BiFunction { entities, expansions ->
            EntityMapper.from(expansions, entities)
        })
    }


    override fun clear() {
        db.toBlocking().delete(ICardEntity::class)
    }
}