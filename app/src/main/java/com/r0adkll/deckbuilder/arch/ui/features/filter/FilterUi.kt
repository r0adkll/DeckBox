package com.r0adkll.deckbuilder.arch.ui.features.filter

import android.os.Parcelable
import com.ftinc.kit.arch.presentation.state.Ui
import com.r0adkll.deckbuilder.arch.domain.Format
import com.r0adkll.deckbuilder.arch.domain.Rarity
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.SearchField
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.State.Change.AttributeSelected
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.State.Change.ClearFilter
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.State.Change.ExpansionSelected
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.State.Change.ExpansionsLoaded
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.State.Change.FieldChanged
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.State.Change.RaritySelected
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.State.Change.TypeSelected
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.State.Change.ValueRangeChanged
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterUi.State.Change.ViewMoreSelected
import com.r0adkll.deckbuilder.arch.ui.features.filter.adapter.Item
import com.r0adkll.deckbuilder.util.extensions.expanded
import com.r0adkll.deckbuilder.util.extensions.standard
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.Type
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize

interface FilterUi : Ui<FilterUi.State, FilterUi.State.Change> {

  interface Intentions {

    fun fieldChanges(): Observable<SearchField>
    fun typeClicks(): Observable<Pair<String, Type>>
    fun attributeClicks(): Observable<FilterAttribute>
    fun optionClicks(): Observable<Pair<String, Any>>
    fun viewMoreClicks(): Observable<Unit>
    fun valueRangeChanges(): Observable<Pair<String, Item.ValueRange.Value>>
    fun clearFilter(): Observable<Unit>
  }

  interface Actions {

    fun setItems(items: List<Item>)
    fun setIsEmpty(isEmpty: Boolean)
  }

  enum class ExpansionVisibility(val next: () -> ExpansionVisibility) {
    STANDARD({ EXPANDED }),
    EXPANDED({ UNLIMITED }),
    UNLIMITED({ UNLIMITED })
  }

  sealed class FilterAttribute : Parcelable {

    @Parcelize
    data class SuperTypeAttribute(val superType: SuperType) : FilterAttribute()

    @Parcelize
    data class SubTypeAttribute(val subType: String) : FilterAttribute()

    @Parcelize
    data class ExpansionAttribute(val format: Format, val expansions: List<Expansion>) : FilterAttribute()
  }

  @Parcelize
  data class State(
    val spec: FilterSpec,
    val filter: Filter,
    val visibility: ExpansionVisibility,
    val expansions: List<Expansion>,
    val subTypes: List<String>,
  ) : Ui.State<State.Change>, Parcelable {

    @Suppress("LongMethod", "ComplexMethod")
    override fun reduce(change: Change): State = when (change) {
      is ExpansionsLoaded -> {
        copy(
          expansions = change.expansions,
          spec = FilterSpec.createAll(
            expansions = change.expansions,
            subTypes = subTypes,
            visibility = visibility
          )
        )
      }
      is Change.SubtypesLoaded -> {
        copy(
          subTypes = change.subTypes,
          spec = FilterSpec.createAll(
            expansions = expansions,
            subTypes = change.subTypes,
            visibility = visibility,
          )
        )
      }

      is FieldChanged -> copy(filter = FilterReducer.reduceField(change.field, filter))
      is TypeSelected -> copy(filter = FilterReducer.reduceType(change.key, change.type, filter))

      is AttributeSelected -> {
        val newFilter = FilterReducer.reduceAttribute(change.attribute, filter)
        val visibility = if (change.attribute is FilterAttribute.ExpansionAttribute) {
          when {
            newFilter.expansions.containsAll(expansions.expanded()) -> ExpansionVisibility.EXPANDED
            newFilter.expansions.containsAll(expansions.standard()) -> ExpansionVisibility.STANDARD
            else -> visibility
          }
        } else {
          visibility
        }
        copy(
          spec = FilterSpec.createAll(expansions, subTypes, visibility),
          filter = newFilter,
          visibility = visibility
        )
      }

      is ExpansionSelected -> copy(filter = FilterReducer.reduceExpansion(change.expansion, filter))
      is RaritySelected -> copy(filter = FilterReducer.reduceRarity(change.rarity, filter))
      is ValueRangeChanged -> copy(filter = FilterReducer.reduceValueRange(change.key, change.value, filter))

      ViewMoreSelected -> copy(
        visibility = visibility.next(),
        spec = FilterSpec.createAll(expansions, subTypes, visibility.next())
      )

      ClearFilter -> copy(filter = Filter.DEFAULT)
    }

    override fun toString(): String {
      return "State(spec=$spec, filter=$filter, visibility=$visibility, expansions=${expansions.size})"
    }

    fun applySpecification(): List<Item> = spec.apply(filter)

    sealed class Change(logText: String) : Ui.State.Change(logText) {
      class ExpansionsLoaded(val expansions: List<Expansion>) : Change("network -> expansions loaded")
      class SubtypesLoaded(val subTypes: List<String>) : Change("network -> subtypes loaded")
      class FieldChanged(val field: SearchField) : Change("user -> $field was selected")
      class TypeSelected(val key: String, val type: Type) : Change("user -> $type was selected")
      class AttributeSelected(val attribute: FilterAttribute) : Change("user -> $attribute was selected")
      class ExpansionSelected(val expansion: Expansion) : Change("user -> $expansion was selected")
      class RaritySelected(val rarity: Rarity) : Change("user -> $rarity was selected")
      class ValueRangeChanged(val key: String, val value: String) : Change("user -> $key was changed to $value")
      object ViewMoreSelected : Change("user -> view more expansions selected")
      object ClearFilter : Change("user -> clear filter")
    }

    companion object {

      val DEFAULT by lazy {
        State(
          spec = FilterSpec.createAll(emptyList(), emptyList(), ExpansionVisibility.STANDARD),
          filter = Filter.DEFAULT,
          visibility = ExpansionVisibility.STANDARD,
          expansions = emptyList(),
          subTypes = emptyList(),
        )
      }
    }
  }
}
