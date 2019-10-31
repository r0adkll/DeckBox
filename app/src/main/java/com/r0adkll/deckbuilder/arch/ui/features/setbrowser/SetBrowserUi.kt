package com.r0adkll.deckbuilder.arch.ui.features.setbrowser

import android.os.Parcelable
import com.ftinc.kit.arch.presentation.BaseActions
import com.ftinc.kit.arch.presentation.state.BaseState
import com.ftinc.kit.arch.presentation.state.Ui
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize

interface SetBrowserUi : Ui<SetBrowserUi.State, SetBrowserUi.State.Change> {

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
            val isPreview: Boolean,
            override val isLoading: Boolean,
            override val error: String?,
            val cards: List<PokemonCard>,
            val filter: BrowseFilter,
            val pageSize: Int = 1000
    ) : BaseState<State.Change>(isLoading, error), Parcelable {

        override fun reduce(change: Change): State = when(change) {
            Change.IsLoading -> this.copy(isLoading = true, error = null)
            is Change.Error -> this.copy(error = change.description, isLoading = false)
            is Change.FilterChanged -> this.copy(filter = change.filter)
            is Change.CardsLoaded -> this.copy(cards = change.cards, isLoading = false)
        }

        sealed class Change(logText: String) : Ui.State.Change(logText) {
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
                State("sm5", false, false, null, emptyList(), BrowseFilter.ALL)
            }
        }
    }
}
