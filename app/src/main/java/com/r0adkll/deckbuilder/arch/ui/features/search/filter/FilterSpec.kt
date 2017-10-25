package com.r0adkll.deckbuilder.arch.ui.features.search.filter


import android.support.annotation.StringRes
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.Rarity
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.FilterUi.ExpansionVisibility.*
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.adapter.Item
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.adapter.Item.Option.*
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.adapter.Item.ValueRange.Modifier.*
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.adapter.Item.ValueRange.Value
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.Type
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


@PaperParcel
data class FilterSpec(val specs: List<Spec>) : PaperParcelable {

    fun apply(filter: Filter): List<Item> = specs.flatMap { it.apply(filter) }


    sealed class Spec : PaperParcelable {

        /**
         * Take a filter and apply the spec to it to generate a list of UI Items
         * to be renderered in the RecyclerView
         */
        abstract fun apply(filter: Filter): List<Item>


        @PaperParcel
        class TypeSpec(val key: String, @StringRes val title: Int) : Spec() {

            override fun apply(filter: Filter): List<Item> = listOf(
                    Item.Header(title),
                    Item.Type(key, getSelected(filter))
            )


            private fun getSelected(filter: Filter): List<Type> = when(key) {
                "type" -> filter.types
                "weaknesses" -> filter.weaknesses
                "resistances" -> filter.resistances
                else -> emptyList()
            }

            companion object {
                @JvmField val CREATOR = PaperParcelFilterSpec_Spec_TypeSpec.CREATOR
            }
        }


        @PaperParcel
        class AttributeSpec(val attributes: List<SubType>) : Spec() {

            override fun apply(filter: Filter): List<Item> = listOf(
                    Item.Header(R.string.filter_header_attributes),
                    Item.Attribute(attributes, filter.subTypes)
            )

            companion object {
                @JvmField val CREATOR = PaperParcelFilterSpec_Spec_AttributeSpec.CREATOR
            }
        }


        @PaperParcel
        class ExpansionSpec(
                val expansions: List<Expansion>,
                val visibility: FilterUi.ExpansionVisibility
        ) : Spec() {

            override fun apply(filter: Filter): List<Item> = listOf(
                    Item.Header(R.string.filter_header_expansions)
            ).plus(getVisibleExpansions(filter))


            private fun getVisibleExpansions(filter: Filter): List<Item> {
                return expansions.filter {
                    when(visibility) {
                        STANDARD -> it.standardLegal
                        EXPANDED -> it.expandedLegal
                        else -> true
                    }
                }.map {
                    ExpansionOption("expansion", it, filter.expansions.contains(it))
                }
            }

            companion object {
                @JvmField val CREATOR = PaperParcelFilterSpec_Spec_ExpansionSpec.CREATOR
            }
        }


        @PaperParcel
        class RaritySpec(val rarities: List<Rarity>) : Spec() {

            override fun apply(filter: Filter): List<Item> = listOf(
                    Item.Header(R.string.filter_header_rarity)
            ).plus(getRarityItems(filter))


            private fun getRarityItems(filter: Filter): List<Item> {
                return rarities.map {
                    RarityOption("rarity", it, filter.rarity.contains(it))
                }
            }

            companion object {
                @JvmField val CREATOR = PaperParcelFilterSpec_Spec_RaritySpec.CREATOR
            }
        }


        @PaperParcel
        class ValueRangeSpec(val key: String,
                             @StringRes val title: Int,
                             val min: Int,
                             val max: Int) : Spec() {

            override fun apply(filter: Filter): List<Item> = listOf(
                    Item.Header(title),
                    Item.ValueRange(key, min, max, getValue(filter))
            )


            private fun getValue(filter: Filter): Value = when(key) {
                "hp" -> parseValue(filter.hp)
                "attackCost" -> parseValue(filter.attackCost)
                "attackDamage" -> parseValue(filter.attackDamage)
                "retreatCost" -> parseValue(filter.retreatCost)
                else -> Value(-1, NONE)
            }


            private fun parseValue(value: String?): Value {
                return value?.let {
                    val modifier = when {
                        value.startsWith("gt", true) -> GREATER_THAN
                        value.startsWith("gte", true) -> GREATER_THAN_EQUALS
                        value.startsWith("lt", true) -> LESS_THAN
                        value.startsWith("lte", true) -> LESS_THAN_EQUALS
                        else -> NONE
                    }
                    val cleanValue = value
                            .replace("gt", "", true)
                            .replace("gte", "", true)
                            .replace("lt", "", true)
                            .replace("lte", "", true)
                            .trim()
                    cleanValue.toIntOrNull()?.let {
                        Value(it, modifier)
                    } ?: Value(-1, NONE)
                } ?: Value(-1, NONE)
            }

            companion object {
                @JvmField val CREATOR = PaperParcelFilterSpec_Spec_ValueRangeSpec.CREATOR
            }
        }
    }


    companion object {
        @JvmField val CREATOR = PaperParcelFilterSpec.CREATOR

        val DEFAULT by lazy {
            createPokemon(emptyList(), STANDARD)
        }


        fun create(superType: SuperType,
                   expansions: List<Expansion> = emptyList(),
                   visibility: FilterUi.ExpansionVisibility): FilterSpec = when(superType) {
            SuperType.POKEMON -> createPokemon(expansions, visibility)
            SuperType.TRAINER -> createTrainer(expansions, visibility)
            SuperType.ENERGY -> createEnergy(expansions, visibility)
            else -> createPokemon(expansions, visibility)
        }


        fun createPokemon(expansions: List<Expansion>,
                          visibility: FilterUi.ExpansionVisibility): FilterSpec {
            return FilterSpec(
                    listOf(
                            Spec.TypeSpec("type", R.string.filter_header_type),
                            Spec.AttributeSpec(listOf(
                                    SubType.BASIC,
                                    SubType.STAGE_1,
                                    SubType.STAGE_2,
                                    SubType.MEGA,
                                    SubType.EX,
                                    SubType.GX,
                                    SubType.LEVEL_UP,
                                    SubType.BREAK,
                                    SubType.LEGEND,
                                    SubType.RESTORED
                            )),
                            Spec.ExpansionSpec(expansions, visibility),
                            Spec.RaritySpec(Rarity.values().toList()),
                            Spec.ValueRangeSpec("retreatCost", R.string.filter_header_retreat_cost, -1, 4),
                            Spec.ValueRangeSpec("attackCost", R.string.filter_header_attack_cost, -1, 5),
                            Spec.ValueRangeSpec("attackDamage", R.string.filter_header_attack_damage, -1, 300),
                            Spec.ValueRangeSpec("hp", R.string.filter_header_retreat_cost, -1, 250),
                            Spec.TypeSpec("weaknesses", R.string.filter_header_weaknesses),
                            Spec.TypeSpec("resistances", R.string.filter_header_resistances)
                    )
            )
        }


        fun createTrainer(expansions: List<Expansion>,
                          visibility: FilterUi.ExpansionVisibility): FilterSpec {
            return FilterSpec(
                    listOf(
                            Spec.AttributeSpec(listOf(
                                    SubType.ITEM,
                                    SubType.STADIUM,
                                    SubType.SUPPORTER,
                                    SubType.STADIUM,
                                    SubType.TECHNICAL_MACHINE,
                                    SubType.POKEMON_TOOL,
                                    SubType.ROCKETS_SECRET_MACHINE
                            )),
                            Spec.ExpansionSpec(expansions, visibility),
                            Spec.RaritySpec(Rarity.values().toList())
                    )
            )
        }


        fun createEnergy(expansions: List<Expansion>,
                         visibility: FilterUi.ExpansionVisibility): FilterSpec {
            return FilterSpec(
                    listOf(
                            Spec.AttributeSpec(listOf(
                                    SubType.BASIC,
                                    SubType.SPECIAL
                            )),
                            Spec.ExpansionSpec(expansions, visibility),
                            Spec.RaritySpec(Rarity.values().toList())
                    )
            )
        }
    }

}