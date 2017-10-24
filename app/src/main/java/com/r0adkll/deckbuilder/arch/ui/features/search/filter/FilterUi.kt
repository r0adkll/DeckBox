package com.r0adkll.deckbuilder.arch.ui.features.search.filter

import com.r0adkll.deckbuilder.arch.domain.Rarity
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.ui.components.renderers.StateRenderer
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.FilterUi.State.Change.*
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.adapter.Item
import io.pokemontcg.model.SubType
import io.pokemontcg.model.Type
import io.reactivex.Observable
import org.w3c.dom.Attr
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


interface FilterUi : StateRenderer<FilterUi.State> {

    val state: State


    interface Intentions {

        fun typeClicks(): Observable<Pair<String, Type>>
        fun attributeClicks(): Observable<SubType>
        fun optionClicks(): Observable<Pair<String, Any>>
        fun viewMoreClicks(): Observable<Unit>
        fun valueRangeChanges(): Observable<Pair<String, Item.ValueRange.Value>>
    }


    interface Actions {

        fun setItems(items: List<Item>)
    }


    @PaperParcel
    data class State(
            val filter: Filter
    ) : PaperParcelable {

        fun reduce(change: Change): State = when(change) {
            is TypeSelected -> TODO()
            is AttributeSelected -> TODO()
            is ExpansionSelected -> TODO()
            is RaritySelected -> TODO()
            is ValueRangeChanged -> TODO()
            ViewMoreSelected -> TODO()
        }


        sealed class Change(val logText: String) {
            class TypeSelected(val key: String, val type: Type) : Change("user -> $type was selected")
            class AttributeSelected(val subType: SubType) : Change("user -> $subType was selected")
            class ExpansionSelected(val expansion: Expansion) : Change("user -> $expansion was selected")
            class RaritySelected(val rarity: Rarity) : Change("user -> $rarity was selected")
            class ValueRangeChanged(val key: String, val value: String) : Change("user -> $key value was changed to $value")
            object ViewMoreSelected : Change("user -> view more expansions selected")
        }


        companion object {
            @JvmField val CREATOR = PaperParcelFilterUi_State.CREATOR

            val DEFAULT by lazy {
                State(Filter.DEFAULT)
            }
        }
    }
}