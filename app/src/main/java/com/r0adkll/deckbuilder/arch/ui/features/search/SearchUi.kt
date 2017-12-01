package com.r0adkll.deckbuilder.arch.ui.features.search


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
        fun switchCategories(): Observable<SuperType>
        fun searchCards(): Observable<String>
        fun selectCard(): Observable<PokemonCard>
        fun clearSelection(): Observable<Unit>
    }


    interface Actions {

        fun showFilterEmpty(enabled: Boolean)
        fun setQueryText(text: String)
        fun setCategory(superType: SuperType)
        fun setResults(superType: SuperType, cards: List<PokemonCard>)
        fun setSelectedCards(cards: List<PokemonCard>)
        fun showLoading(superType: SuperType, isLoading: Boolean)
        fun showError(superType: SuperType, description: String)
        fun hideError(superType: SuperType)
    }


    @PaperParcel
    data class Result(
            val query: String,
            val filter: Filter,
            val isLoading: Boolean,
            val error: String?,
            val category: SuperType,
            val results: List<PokemonCard>
    ) : PaperParcelable {



        companion object {
            @JvmField val CREATOR = PaperParcelSearchUi_Result.CREATOR

            fun createDefault(superType: SuperType): Result {
                return Result("", Filter.DEFAULT, false, null, superType, emptyList())
            }
        }


        override fun toString(): String {
            return "Result(query='$query', filter=$filter, isLoading=$isLoading, error=$error, category=$category, results=${results.size})"
        }
    }


    @PaperParcel
    data class State(
            val category: SuperType,
            val results: Map<SuperType, Result>,
            val selected: List<PokemonCard>
    ) : PaperParcelable {

        fun current(): Result? = results[category]

        fun reduce(change: Change): State = when(change) {
            is Change.IsLoading -> {
                val newResults = results.toMutableMap()
                newResults[change.category] = newResults[change.category]!!
                        .copy(isLoading = true, error = null)
                this.copy(results = newResults.toMap())
            }
            is Change.Error -> {
                val newResults = results.toMutableMap()
                newResults[change.category] = newResults[change.category]!!
                        .copy(error = change.description, isLoading = false)
                this.copy(results = newResults.toMap())
            }
            is Change.QuerySubmitted -> {
                val newResults = results.toMutableMap()
                newResults[change.category] = newResults[change.category]!!.copy(query = change.query)
                this.copy(results = newResults.toMap())
            }
            is Change.FilterChanged -> {
                val newResults = results.toMutableMap()
                val result = newResults[change.category]!!
                newResults[change.category] = result
                        .copy(filter = change.filter, results = if (change.filter.isEmpty && result.query.isBlank()) emptyList() else result.results)
                this.copy(results = newResults.toMap())
            }
            is Change.ResultsLoaded -> {
                val newResults = results.toMutableMap()
                newResults[change.category] = newResults[change.category]!!
                        .copy(results = change.results, isLoading = false, error = null)
                this.copy(results = newResults.toMap())
            }
            is Change.ClearQuery -> {
                val newResults = results.toMutableMap()
                newResults[change.category] = newResults[change.category]!!
                        .copy(results = emptyList(), query = "", isLoading = false, error = null)
                this.copy(results = newResults.toMap())
            }
            is Change.CardSelected -> this.copy(selected = selected.plus(change.pokemonCard))
            is Change.CategorySwitched -> this.copy(category = change.category)
            Change.ClearSelectedCards -> this.copy(selected = emptyList())
        }

        override fun toString(): String {
            return "State(category=$category, results=$results, selected=${selected.size})"
        }


        sealed class Change(val logText: String) {
            class CategorySwitched(val category: SuperType) : Change("user -> switching category to $category")
            class IsLoading(val category: SuperType) : Change("network -> loading search results")
            class Error(val category: SuperType, val description: String) : Change("error -> $description")
            class QuerySubmitted(val category: SuperType, val query: String) : Change("user -> querying $query")
            class FilterChanged(val category: SuperType, val filter: Filter) : Change("user -> filter changed $filter for $category")
            class ResultsLoaded(val category: SuperType, val results: List<PokemonCard>) : Change("network -> search results loaded (${results.size})")
            class ClearQuery(val category: SuperType) : Change("user -> clearing query and results")
            class CardSelected(val pokemonCard: PokemonCard) : Change("user -> selected ${pokemonCard.name}")
            object ClearSelectedCards : Change("user -> cleared selected cards")
        }

        companion object {
            @JvmField val CREATOR = PaperParcelSearchUi_State.CREATOR

            val DEFAULT by lazy {
                State(SuperType.POKEMON, mapOf(
                        SuperType.POKEMON to Result.createDefault(SuperType.POKEMON),
                        SuperType.TRAINER to Result.createDefault(SuperType.TRAINER),
                        SuperType.ENERGY to Result.createDefault(SuperType.ENERGY),
                        SuperType.UNKNOWN to Result.createDefault(SuperType.UNKNOWN)
                ), emptyList())
            }
        }
    }
}