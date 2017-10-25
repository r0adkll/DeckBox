package com.r0adkll.deckbuilder.arch.domain.features.cards.model


import com.r0adkll.deckbuilder.arch.domain.Rarity
import io.pokemontcg.model.SubType
import io.pokemontcg.model.Type
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


@PaperParcel
data class Filter(
        val types: List<Type>,
        val subTypes: List<SubType>,
        val contains: List<String>,
        val expansions: List<Expansion>,
        val rarity: List<Rarity>,
        val retreatCost: String?,
        val attackCost: String?,
        val attackDamage: String?,
        val hp: String?,
        val weaknesses: List<Type>,
        val resistances: List<Type>
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelFilter.CREATOR

        val DEFAULT by lazy {
            Filter(emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), null, null, null, null,
                    emptyList(), emptyList())
        }
    }
}