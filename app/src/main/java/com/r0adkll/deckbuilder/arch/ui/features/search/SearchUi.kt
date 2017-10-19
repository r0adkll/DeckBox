package com.r0adkll.deckbuilder.arch.ui.features.search


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.renderers.StateRenderer
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


interface SearchUi : StateRenderer<SearchUi.State> {

    val state: State


    interface Intentions {

        fun switchCategories(): Observable<SuperType>
        fun searchCards(): Observable<String>
        fun selectCard(): Observable<PokemonCard>
    }


    interface Actions {

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
            val isLoading: Boolean,
            val error: String?,
            val category: SuperType,
            val results: List<PokemonCard>
    ) : PaperParcelable {
        companion object {
            @JvmField val CREATOR = PaperParcelSearchUi_Result.CREATOR

            fun createDefault(superType: SuperType): Result {
                return Result("", false, null, superType, emptyList())
            }
        }
    }


    @PaperParcel
    data class State(
            val category: SuperType,
            val results: Map<SuperType, Result>,
            val selected: List<PokemonCard>
    ) : PaperParcelable {

        fun reduce(change: Change): State = when(change) {
            Change.IsLoading -> {
                val newResults = results.toMutableMap()
                newResults[category] = newResults[category]!!
                        .copy(isLoading = true, error = null)
                this.copy(results = newResults.toMap())
            }
            is Change.Error -> {
                val newResults = results.toMutableMap()
                newResults[category] = newResults[category]!!
                        .copy(error = change.description, isLoading = false)
                this.copy(results = newResults.toMap())
            }
            is Change.QuerySubmitted -> {
                val newResults = results.toMutableMap()
                newResults[category] = newResults[category]!!.copy(query = change.query)
                this.copy(results = newResults.toMap())
            }
            is Change.ResultsLoaded -> {
                val newResults = results.toMutableMap()
                newResults[category] = newResults[category]!!
                        .copy(results = change.results, isLoading = false, error = null)
                this.copy(results = newResults.toMap())
            }
            is Change.CardSelected -> this.copy(selected = selected.plus(change.pokemonCard))
            is Change.CategorySwitched -> this.copy(category = change.category)
        }


        sealed class Change(val logText: String) {
            object IsLoading : Change("network -> loading search results")
            class Error(val description: String) : Change("error -> $description")
            class CategorySwitched(val category: SuperType) : Change("user -> switching category to $category")
            class QuerySubmitted(val query: String) : Change("user -> querying $query")
            class ResultsLoaded(val results: List<PokemonCard>) : Change("network -> search results loaded (${results.size})")
            class CardSelected(val pokemonCard: PokemonCard) : Change("user -> selected ${pokemonCard.name}")
        }


        companion object {
            @JvmField val CREATOR = PaperParcelSearchUi_State.CREATOR

            val DEFAULT by lazy {
                State(SuperType.POKEMON, mapOf(
                        SuperType.POKEMON to Result.createDefault(SuperType.POKEMON),
                        SuperType.TRAINER to Result.createDefault(SuperType.TRAINER),
                        SuperType.ENERGY to Result.createDefault(SuperType.ENERGY)
                ), emptyList())
            }
        }
    }
}