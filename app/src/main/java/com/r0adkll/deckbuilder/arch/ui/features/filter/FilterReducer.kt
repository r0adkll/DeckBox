package com.r0adkll.deckbuilder.arch.ui.features.filter

import com.r0adkll.deckbuilder.arch.domain.Rarity
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.FilterAttribute
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.FilterAttribute.SubTypeAttribute
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.FilterAttribute.SuperTypeAttribute
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.Type


object FilterReducer {

    fun reduceType(key: String, type: Type, filter: Filter): Filter {
        return when(key) {
            "type" -> filter.copy(types = filter.types.toggle(type))
            "weaknesses" -> filter.copy(weaknesses = filter.weaknesses.toggle(type))
            "resistances" -> filter.copy(resistances = filter.resistances.toggle(type))
            else -> filter
        }
    }


    fun reduceAttribute(attribute: FilterAttribute, filter: Filter): Filter = when(attribute) {
        is SuperTypeAttribute -> filter.copy(superType = filter.toggle(attribute.superType))
        is SubTypeAttribute -> filter.copy(subTypes = filter.subTypes.toggle(attribute.subType))
        is FilterAttribute.ContainsAttribute -> filter.copy(contains = filter.contains.toggle(attribute.attribute))
    }


    fun reduceExpansion(expansion: Expansion, filter: Filter): Filter {
        return filter.copy(expansions = filter.expansions.toggle(expansion))
    }


    fun reduceRarity(rarity: Rarity, filter: Filter): Filter {
        return filter.copy(rarity = filter.rarity.toggle(rarity))
    }


    fun reduceValueRange(key: String, value: String?, filter: Filter): Filter {
        return when(key) {
            "hp" -> filter.copy(hp = value)
            "attackCost" -> filter.copy(attackCost = value)
            "attackDamage" -> filter.copy(attackDamage = value)
            "retreatCost" -> filter.copy(retreatCost = value)
            else -> filter
        }
    }


    private fun <T> List<T>.toggle(value: T): List<T> {
        return if (this.contains(value)) {
            this.minus(value)
        } else {
            this.plus(value)
        }
    }


    private fun Filter.toggle(value: SuperType): SuperType? =
            if (this.superType == value) null else value
}