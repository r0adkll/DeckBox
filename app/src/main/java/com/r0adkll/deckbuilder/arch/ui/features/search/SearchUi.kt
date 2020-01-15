package com.r0adkll.deckbuilder.arch.ui.features.search

import com.ftinc.kit.arch.presentation.state.Ui
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import io.reactivex.Observable
import paperparcel.PaperParcel
import paperparcel.PaperParcelable
import java.util.*

interface SearchUi : Ui<SearchUi.State, SearchUi.State.Change> {

    interface Intentions {

        fun filterUpdates(): Observable<Filter>
        fun searchCards(): Observable<String>
        fun selectCard(): Observable<PokemonCard>
        fun removeCard(): Observable<PokemonCard>
    }

    interface Actions {

        fun showFilterEmpty(enabled: Boolean)
        fun setQueryText(text: String)
        fun setResults(cards: List<PokemonCard>)
        fun setSelectedCards(cards: List<PokemonCard>)
        fun showLoading(isLoading: Boolean)
        fun showEmptyResults()
        fun showEmptyDefault()
        fun showError(description: String)
        fun hideError()
    }

    @PaperParcel
    data class State @JvmOverloads constructor(
        val id: String,
        val deckId: String?,
        val query: String,
        val filter: Filter,
        val isLoading: Boolean,
        val error: String?,
        @Transient val results: List<PokemonCard> = emptyList(),
        @Transient val selected: List<PokemonCard> = emptyList()
    ) : Ui.State<State.Change>, PaperParcelable {

        @Suppress("LongMethod", "ComplexMethod", "NestedBlockDepth")
        override fun reduce(change: Change): State = when (change) {
            Change.IsLoading -> copy(isLoading = true, error = null)
            is Change.Error -> copy(error = change.description, isLoading = false)
            is Change.QuerySubmitted -> copy(query = change.query)
            is Change.FilterChanged -> copy(
                filter = change.filter,
                results = if (change.filter.isEmptyWithoutField && query.isBlank()) {
                    emptyList()
                } else {
                    results
                }
            )
            is Change.ResultsLoaded -> copy(results = change.results, isLoading = false, error = null)
            is Change.ClearQuery -> copy(results = emptyList(), query = "", isLoading = false, error = null)
            is Change.DeckUpdated -> copy(selected = change.deck.cards)
        }

        override fun toString(): String {
            return "State(results=$results, selected=${selected.size})"
        }

        sealed class Change(logText: String) : Ui.State.Change(logText) {
            object IsLoading : Change("network -> loading search results")
            class Error(val description: String) : Change("error -> $description")
            class QuerySubmitted(val query: String) : Change("user -> querying $query")
            class FilterChanged(val filter: Filter) : Change("user -> filter changed $filter")
            class ResultsLoaded(
                val results: List<PokemonCard>
            ) : Change("network -> search results loaded (${results.size})")
            object ClearQuery : Change("user -> clearing query and results")
            class DeckUpdated(val deck: Deck) : Change("data -> deck updated!")
        }

        companion object {
            @JvmField
            val CREATOR = PaperParcelSearchUi_State.CREATOR

            val DEFAULT by lazy {
                State(UUID.randomUUID().toString(),
                    null,
                    "",
                    Filter(),
                    false,
                    null
                )
            }
        }
    }
}
