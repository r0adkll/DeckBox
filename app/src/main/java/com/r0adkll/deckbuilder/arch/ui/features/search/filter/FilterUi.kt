package com.r0adkll.deckbuilder.arch.ui.features.search.filter


import com.r0adkll.deckbuilder.arch.domain.Rarity
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.ui.components.renderers.StateRenderer
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.FilterUi.State.Change.*
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.adapter.Item
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.Type
import io.reactivex.Observable
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


interface FilterUi : StateRenderer<FilterUi.State> {

    val state: State


    interface Intentions {

        fun typeClicks(): Observable<Pair<String, Type>>
        fun attributeClicks(): Observable<FilterAttribute>
        fun optionClicks(): Observable<Pair<String, Any>>
        fun viewMoreClicks(): Observable<Unit>
        fun valueRangeChanges(): Observable<Pair<String, Item.ValueRange.Value>>
    }


    interface Actions {

        fun setItems(items: List<Item>)
    }


    enum class ExpansionVisibility(val next: () -> ExpansionVisibility) {
        STANDARD({ EXPANDED }),
        EXPANDED({ UNLIMITED }),
        UNLIMITED({ UNLIMITED })
    }


    sealed class FilterAttribute : PaperParcelable {

        @PaperParcel
        data class SubTypeAttribute(val subType: SubType) : FilterAttribute() {
            companion object {
                @JvmField val CREATOR = PaperParcelFilterUi_FilterAttribute_SubTypeAttribute.CREATOR
            }
        }


        @PaperParcel
        data class ContainsAttribute(val attribute: String) : FilterAttribute() {
            companion object {
                @JvmField val CREATOR = PaperParcelFilterUi_FilterAttribute_ContainsAttribute.CREATOR
            }
        }
    }


    @PaperParcel
    data class State(
            val spec: FilterSpec,
            val filter: Filter,
            val expansions: List<Expansion>,
            val visibility: ExpansionVisibility
    ) : PaperParcelable {

        fun reduce(change: Change): State = when(change) {
            is ExpansionsLoaded -> this.copy(expansions = change.expansions)
            is CategoryChanged -> this.copy(spec = FilterSpec.create(change.category, expansions, visibility))
            is TypeSelected -> this.copy(filter = FilterReducer.reduceType(change.key, change.type, filter))
            is AttributeSelected -> this.copy(filter = FilterReducer.reduceAttribute(change.attribute, filter))
            is ExpansionSelected -> this.copy(filter = FilterReducer.reduceExpansion(change.expansion, filter))
            is RaritySelected -> this.copy(filter = FilterReducer.reduceRarity(change.rarity, filter))
            is ValueRangeChanged -> this.copy(filter = FilterReducer.reduceValueRange(change.key, change.value, filter))
            ViewMoreSelected -> this.copy(visibility = visibility.next())
        }


        sealed class Change(val logText: String) {
            class ExpansionsLoaded(val expansions: List<Expansion>) : Change("network -> expansions loaded")
            class CategoryChanged(val category: SuperType) : Change("user -> category changed to $category")
            class TypeSelected(val key: String, val type: Type) : Change("user -> $type was selected")
            class AttributeSelected(val attribute: FilterAttribute) : Change("user -> $attribute was selected")
            class ExpansionSelected(val expansion: Expansion) : Change("user -> $expansion was selected")
            class RaritySelected(val rarity: Rarity) : Change("user -> $rarity was selected")
            class ValueRangeChanged(val key: String, val value: String) : Change("user -> $key value was changed to $value")
            object ViewMoreSelected : Change("user -> view more expansions selected")
        }


        companion object {
            @JvmField val CREATOR = PaperParcelFilterUi_State.CREATOR

            val DEFAULT by lazy {
                State(FilterSpec.DEFAULT, Filter.DEFAULT, emptyList(), ExpansionVisibility.STANDARD)
            }
        }
    }
}