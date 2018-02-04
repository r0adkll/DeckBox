package com.r0adkll.deckbuilder.arch.domain.features.cards.model


import com.r0adkll.deckbuilder.arch.domain.Rarity
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.Type
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


@PaperParcel
data class Filter(
        val field: SearchField,
        val types: List<Type>,
        val superType: SuperType?,
        val subTypes: List<SubType>,
        val contains: List<String>,
        val expansions: List<Expansion>,
        val rarity: List<Rarity>,
        val retreatCost: String?,
        val attackCost: String?,
        val attackDamage: String?,
        val hp: String?,
        val weaknesses: List<Type>,
        val resistances: List<Type>,
        val pageSize: Int = 1000
) : PaperParcelable {

    val isEmptyWithoutField: Boolean
        get() {
            return (types.isEmpty() && subTypes.isEmpty() && contains.isEmpty() && expansions.isEmpty()
                    && rarity.isEmpty() && retreatCost.isNullOrBlank() && attackCost.isNullOrBlank()
                    && attackDamage.isNullOrBlank() && hp.isNullOrBlank() && weaknesses.isEmpty()
                    && resistances.isEmpty() && superType == null)
        }

    val isEmpty: Boolean
        get() = isEmptyWithoutField && this.field == SearchField.NAME


    companion object {
        @JvmField val CREATOR = PaperParcelFilter.CREATOR

        val DEFAULT by lazy {
            Filter(SearchField.NAME, emptyList(), null, emptyList(), emptyList(),
                    emptyList(), emptyList(), null, null, null, null,
                    emptyList(), emptyList())
        }
    }

    override fun toString(): String {
        return "Filter(field=$field, types=$types, superType=$superType, subTypes=$subTypes, " +
                "contains=$contains, rarity=$rarity, retreatCost=$retreatCost, attackCost=$attackCost, " +
                "attackDamage=$attackDamage, hp=$hp, weaknesses=$weaknesses, resistances=$resistances)"
    }
}