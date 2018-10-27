package com.r0adkll.deckbuilder.arch.domain.features.cards.model


import com.r0adkll.deckbuilder.arch.domain.Rarity
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.Type
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


/**
 * Search filter master object. Any changes or updates to this class must be propagated to it's
 * respected query mappers for network and local requests
 *
 * @see com.r0adkll.deckbuilder.arch.data.mappings.FilterMapper
 * @see com.r0adkll.deckbuilder.arch.data.database.util.FilterQueryHelper
 */
@PaperParcel
data class Filter(
        val field: SearchField = SearchField.NAME,
        val types: List<Type> = emptyList(),
        val superType: SuperType? = null,
        val subTypes: List<SubType> = emptyList(),
        val contains: List<String> = emptyList(),
        val expansions: List<Expansion> = emptyList(),
        val rarity: List<Rarity> = emptyList(),
        val retreatCost: String? = null,
        val attackCost: String? = null,
        val attackDamage: String? = null,
        val hp: String? = null,
        val evolvesFrom: String? = null,
        val weaknesses: List<Type> = emptyList(),
        val resistances: List<Type> = emptyList(),
        val pageSize: Int = 1000
) : PaperParcelable {

    val isEmptyWithoutField: Boolean
        get() {
            return (types.isEmpty() && subTypes.isEmpty() && contains.isEmpty() && expansions.isEmpty()
                    && rarity.isEmpty() && retreatCost.isNullOrBlank() && attackCost.isNullOrBlank()
                    && attackDamage.isNullOrBlank() && hp.isNullOrBlank() && weaknesses.isEmpty()
                    && evolvesFrom.isNullOrBlank() && resistances.isEmpty() && superType == null)
        }

    val isEmpty: Boolean
        get() = isEmptyWithoutField && this.field == SearchField.NAME


    companion object {
        @JvmField val CREATOR = PaperParcelFilter.CREATOR

        val DEFAULT by lazy {
            Filter()
        }
    }

    override fun toString(): String {
        return "Filter(field=$field, types=$types, superType=$superType, subTypes=$subTypes, " +
                "contains=$contains, rarity=$rarity, retreatCost=$retreatCost, attackCost=$attackCost, " +
                "attackDamage=$attackDamage, hp=$hp, evolvesFrom=$evolvesFrom, weaknesses=$weaknesses, " +
                "resistances=$resistances)"
    }
}