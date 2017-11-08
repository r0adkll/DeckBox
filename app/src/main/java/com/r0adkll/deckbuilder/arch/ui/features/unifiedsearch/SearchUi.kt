package com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.renderers.StateRenderer
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


interface SearchUi : StateRenderer<SearchUi.State> {

    val state: State


    interface Intentions {

        fun filterUpdates(): Observable<Pair<SuperType, Filter>>
        fun searchCards(): Observable<String>
    }


    interface Actions {

        fun showFilterEmpty(enabled: Boolean)
        fun setQueryText(text: String)
        fun setResults(scards: List<PokemonCard>)
        fun showLoading(isLoading: Boolean)
        fun showError(description: String)
        fun hideError()
    }


    @PaperParcel
    data class State(
            val query: String,
            val filter: Filter,
            val isLoading: Boolean,
            val error: String?,
            val results: List<PokemonCard>
    ) : PaperParcelable {

        fun reduce(change: Change): State = when(change) {
            Change.IsLoading -> this.copy(isLoading = true, error = null)
            Change.ClearQuery -> this.copy(query = "", results = emptyList(), isLoading = false, error = null)
            is Change.Error -> this.copy(error = change.description, isLoading = false)
            is Change.QuerySubmitted -> this.copy(query = change.query)
            is Change.FilterChanged -> this.copy(filter = change.filter,
                    results = if (change.filter.isEmpty && query.isBlank()) emptyList() else results)
            is Change.ResultsLoaded -> this.copy(results = change.results, error = null, isLoading = false)
        }

        sealed class Change(val logText: String) {
            object IsLoading : Change("network -> loading search results")
            object ClearQuery : Change("user -> clearing query and results")

            class Error(val description: String) : Change("error -> $description")
            class QuerySubmitted(val query: String) : Change("user -> querying $query")
            class FilterChanged(val filter: Filter) : Change("user -> filter changed $filter")
            class ResultsLoaded(val results: List<PokemonCard>) : Change("network -> search results loaded (${results.size})")
        }


        companion object {
            @JvmField val CREATOR = PaperParcelSearchUi_State.CREATOR
        }
    }
}