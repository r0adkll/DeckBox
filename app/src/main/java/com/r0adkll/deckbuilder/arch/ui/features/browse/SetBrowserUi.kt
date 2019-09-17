package com.r0adkll.deckbuilder.arch.ui.features.browse


import android.os.Parcelable
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseActions
import com.r0adkll.deckbuilder.arch.ui.components.renderers.StateRenderer
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize


interface SetBrowserUi : StateRenderer<SetBrowserUi.State> {

    val state: State


    interface Intentions {

        fun filterChanged(): Observable<BrowseFilter>
    }


    interface Actions : BaseActions {

        fun setFilter(filter: BrowseFilter)
        fun setCards(cards: List<PokemonCard>)
        fun hideFilters(vararg filters: BrowseFilter)
    }


    enum class BrowseFilter {
        ALL,
        POKEMON,
        TRAINER,
        ENERGY,
        GX,
        TAG_TEAM,
        PRISM
    }


    @Parcelize
    data class State(
            val setCode: String,
            val isLoading: Boolean,
            val error: String?,
            val cards: List<PokemonCard>,
            val filter: BrowseFilter,
            val pageSize: Int = 1000
    ) : Parcelable {

        val searchFilter: Filter
            get() = Filter.DEFAULT.copy(expansions = listOf(Expansion(setCode, null, "", "", 0, false, false, "", "", "")), pageSize = pageSize)


        fun reduce(change: Change): State = when(change) {
            Change.IsLoading -> this.copy(isLoading = true, error = null)
            is Change.Error -> this.copy(error = change.description, isLoading = false)
            is Change.FilterChanged -> this.copy(filter = change.filter)
            is Change.CardsLoaded -> this.copy(cards = change.cards, isLoading = false)
        }


        sealed class Change(val logText: String) {
            object IsLoading : Change("network -> loading cards from set")
            class Error(val description: String) : Change("error -> $description")
            class FilterChanged(val filter: BrowseFilter) : Change("user -> filter changed: ${filter.name}")
            class CardsLoaded(val cards: List<PokemonCard>) : Change("network -> ${cards.size} cards loaded")
        }


        override fun toString(): String {
            return "State(setCode=$setCode, isLoading=$isLoading, error=$error, cards=${cards.size}, filter=$filter)"
        }

        companion object {

            val DEFAULT by lazy {
                State("sm5", false, null, emptyList(), BrowseFilter.ALL)
            }
        }
    }
}
