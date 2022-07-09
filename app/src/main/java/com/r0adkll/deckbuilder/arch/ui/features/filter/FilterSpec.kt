package com.r0adkll.deckbuilder.arch.ui.features.filter

import android.os.Parcelable
import androidx.annotation.StringRes
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.Format
import com.r0adkll.deckbuilder.arch.domain.Rarity
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterSpec.Spec.AttributeSpec
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.ExpansionVisibility.EXPANDED
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.ExpansionVisibility.STANDARD
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.ExpansionVisibility.UNLIMITED
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.FilterAttribute
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.FilterAttribute.ExpansionAttribute
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.FilterAttribute.SubTypeAttribute
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.FilterAttribute.SuperTypeAttribute
import com.r0adkll.deckbuilder.arch.ui.features.filter.adapter.Item
import com.r0adkll.deckbuilder.arch.ui.features.filter.adapter.Item.Option.ExpansionOption
import com.r0adkll.deckbuilder.arch.ui.features.filter.adapter.Item.Option.RarityOption
import com.r0adkll.deckbuilder.arch.ui.features.filter.adapter.Item.ValueRange.Modifier.GREATER_THAN
import com.r0adkll.deckbuilder.arch.ui.features.filter.adapter.Item.ValueRange.Modifier.GREATER_THAN_EQUALS
import com.r0adkll.deckbuilder.arch.ui.features.filter.adapter.Item.ValueRange.Modifier.LESS_THAN
import com.r0adkll.deckbuilder.arch.ui.features.filter.adapter.Item.ValueRange.Modifier.LESS_THAN_EQUALS
import com.r0adkll.deckbuilder.arch.ui.features.filter.adapter.Item.ValueRange.Modifier.NONE
import com.r0adkll.deckbuilder.arch.ui.features.filter.adapter.Item.ValueRange.Value
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.Type
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterSpec(val specs: List<Spec>) : Parcelable {

  fun apply(filter: Filter): List<Item> = specs.flatMap { it.apply(filter) }

  sealed class Spec : Parcelable {

    /**
     * Take a filter and apply the spec to it to generate a list of UI Items
     * to be renderered in the RecyclerView
     */
    abstract fun apply(filter: Filter): List<Item>

    override fun toString(): String {
      return javaClass.simpleName
    }

    @Parcelize
    object FieldSpec : Spec() {

      override fun apply(filter: Filter): List<Item> = listOf(
        Item.Header(R.string.filter_header_search_field),
        Item.Field(filter.field)
      )
    }

    @Parcelize
    class TypeSpec(val key: String, @StringRes val title: Int) : Spec() {

      override fun apply(filter: Filter): List<Item> = listOf(
        Item.Header(title),
        Item.Type(key, getSelected(filter))
      )

      private fun getSelected(filter: Filter): List<Type> = when (key) {
        "type" -> filter.types
        "weaknesses" -> filter.weaknesses
        "resistances" -> filter.resistances
        else -> emptyList()
      }
    }

    @Parcelize
    class AttributeSpec(val attributes: List<FilterAttribute>) : Spec() {

      override fun apply(filter: Filter): List<Item> = listOf(
        Item.Header(R.string.filter_header_attributes),
        Item.Attribute(attributes, getFilteredAttributes(filter))
      )

      private fun getFilteredAttributes(filter: Filter): List<FilterAttribute> {
        val attrs = ArrayList<FilterAttribute>()
        attrs += filter.subTypes.map { SubTypeAttribute(it) }
        filter.superType?.let { attrs += SuperTypeAttribute(it) }
        return attrs
      }
    }

    @Parcelize
    class ExpansionSpec(
      val expansions: List<Expansion>,
      val visibility: FilterUi.ExpansionVisibility
    ) : Spec() {

      override fun apply(filter: Filter): List<Item> {
        val items = ArrayList<Item>()
        items += Item.Header(R.string.filter_header_expansions)

        val attributes = listOf(
          ExpansionAttribute(Format.STANDARD, expansions),
          ExpansionAttribute(Format.EXPANDED, expansions)
        )
        val selectedAttributes = getSelectedFormatAttributes(filter)
        items += Item.Attribute(attributes, selectedAttributes)

        items += getVisibleExpansions(filter)
        if (visibility != UNLIMITED) {
          items += Item.ViewMore(
            when (visibility) {
              STANDARD -> R.string.filter_viewmore_expanded
              else -> R.string.filter_viewmore_all
            }
          )
        }
        return items
      }

      private fun getVisibleExpansions(filter: Filter): List<Item> {
        return expansions.filter {
          when (visibility) {
            STANDARD -> it.standardLegal
            EXPANDED -> it.expandedLegal
            else -> true
          }
        }.map {
          ExpansionOption("expansion", it, filter.expansions.contains(it))
        }
      }

      private fun getSelectedFormatAttributes(filter: Filter): List<FilterAttribute> {
        val standardExpansions = expansions.filter { it.standardLegal }
        val expandedExpansions = expansions.filter { it.expandedLegal }
        val attrs = ArrayList<ExpansionAttribute>()
        if (filter.expansions.containsAll(standardExpansions)) {
          attrs += ExpansionAttribute(Format.STANDARD, expansions)
        }
        if (filter.expansions.containsAll(expandedExpansions)) {
          attrs += ExpansionAttribute(Format.EXPANDED, expansions)
        }
        return attrs
      }
    }

    @Parcelize
    class RaritySpec(val rarities: List<Rarity>) : Spec() {

      override fun apply(filter: Filter): List<Item> = listOf(
        Item.Header(R.string.filter_header_rarity)
      ).plus(getRarityItems(filter))

      private fun getRarityItems(filter: Filter): List<Item> {
        return rarities.map {
          RarityOption("rarity", it, filter.rarity.contains(it))
        }
      }
    }

    @Parcelize
    class ValueRangeSpec(
      val key: String,
      @StringRes val title: Int,
      val min: Int,
      val max: Int
    ) : Spec() {

      override fun apply(filter: Filter): List<Item> = listOf(
        Item.Header(title),
        Item.ValueRange(key, min, max, getValue(filter))
      )

      private fun getValue(filter: Filter): Value = when (key) {
        "hp" -> parseValue(filter.hp)
        "attackCost" -> parseValue(filter.attackCost)
        "attackDamage" -> parseValue(filter.attackDamage)
        "retreatCost" -> parseValue(filter.retreatCost)
        else -> Value(0, NONE)
      }

      companion object {

        fun parseValue(value: String?): Value {
          return value?.let {
            val modifier = when {
              value.startsWith("gte", true) -> GREATER_THAN_EQUALS
              value.startsWith("lte", true) -> LESS_THAN_EQUALS
              value.startsWith("gt", true) -> GREATER_THAN
              value.startsWith("lt", true) -> LESS_THAN
              else -> NONE
            }
            val cleanValue = value
              .replace("gte", "", true)
              .replace("lte", "", true)
              .replace("gt", "", true)
              .replace("lt", "", true)
              .trim()

            cleanValue.toIntOrNull()?.let {
              Value(it, modifier)
            } ?: Value(0, NONE)
          } ?: Value(0, NONE)
        }
      }
    }
  }

  companion object {

    private val retreatCostRange = 0..4
    private val attackCostRange = 0..5
    private val attackDmgRange = 0..300
    private val hpRange = 0..250

    val DEFAULT by lazy {
      createAll(emptyList(), emptyList(), STANDARD)
    }

    fun createAll(
      expansions: List<Expansion>,
      subTypes: List<String>,
      visibility: FilterUi.ExpansionVisibility
    ): FilterSpec {
      return FilterSpec(
        listOf(
          Spec.FieldSpec,
          Spec.TypeSpec("type", R.string.filter_header_type),
          AttributeSpec(
            listOf(
              SuperTypeAttribute(SuperType.POKEMON),
              SuperTypeAttribute(SuperType.TRAINER),
              SuperTypeAttribute(SuperType.ENERGY),
              *subTypes.map { SubTypeAttribute(it) }.toTypedArray(),
            )
          ),
          Spec.ExpansionSpec(expansions, visibility),
          Spec.RaritySpec(Rarity.values().toList()),
          Spec.ValueRangeSpec(
            "retreatCost", R.string.filter_header_retreat_cost,
            retreatCostRange.first, retreatCostRange.last
          ),
          Spec.ValueRangeSpec(
            "attackCost", R.string.filter_header_attack_cost,
            attackCostRange.first, attackCostRange.last
          ),
          Spec.ValueRangeSpec(
            "attackDamage", R.string.filter_header_attack_damage,
            attackDmgRange.first, attackDmgRange.last
          ),
          Spec.ValueRangeSpec("hp", R.string.filter_header_retreat_cost, hpRange.first, hpRange.last),
          Spec.TypeSpec("weaknesses", R.string.filter_header_weaknesses),
          Spec.TypeSpec("resistances", R.string.filter_header_resistances)
        )
      )
    }
  }
}
