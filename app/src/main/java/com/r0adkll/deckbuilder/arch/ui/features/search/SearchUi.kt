package com.r0adkll.deckbuilder.arch.ui.features.search


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseActions
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
    }


    interface Actions: BaseActions {

        fun setCategory(superType: SuperType)
        fun setResults(cards: List<PokemonCard>)
    }


    @PaperParcel
    data class State(
            val isLoading: Boolean,
            val error: String?,
            val category: SuperType,
            val results: List<PokemonCard>
    ) : PaperParcelable {

        fun reduce(change: Change): State = when(change) {
            Change.IsLoading -> this.copy(isLoading = true, error = null)
            is Change.Error -> this.copy(isLoading = false, error = change.description)
            is Change.CategorySwitched -> this.copy(category = change.category, isLoading = false, error = null, results = emptyList())
            is Change.ResultsLoaded -> this.copy(results = change.results, isLoading = false, error = null)
        }


        sealed class Change(val logText: String) {
            object IsLoading : Change("network -> loading search results")
            class Error(val description: String) : Change("error -> $description")
            class CategorySwitched(val category: SuperType) : Change("user -> switching category to $category")
            class ResultsLoaded(val results: List<PokemonCard>) : Change("network -> search results loaded (${results.size})")
        }


        companion object {
            @JvmField val CREATOR = PaperParcelSearchUi_State.CREATOR

            val DEFAULT by lazy {
                State(false, null, SuperType.POKEMON, emptyList())
            }
        }
    }
}