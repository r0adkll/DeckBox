package com.r0adkll.deckbuilder.arch.ui.features.browser


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.ui.components.BaseActions
import com.r0adkll.deckbuilder.arch.ui.components.renderers.StateRenderer
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


interface BrowseUi : StateRenderer<BrowseUi.State> {

    val state: State


    interface Actions : BaseActions {

        fun setExpansions(expansions: List<Expansion>)
    }


    @PaperParcel
    data class State(
            val isLoading: Boolean,
            val error: String?,
            val expansions: List<Expansion>
    ) : PaperParcelable {

        fun reduce(change: Change): State = when(change) {
            Change.IsLoading -> this.copy(isLoading = true, error = null)
            is Change.Error -> this.copy(error = change.description, isLoading = false)
            is Change.ExpansionsLoaded -> this.copy(expansions = change.expansions, isLoading = false)
        }


        sealed class Change(val logText: String) {
            object IsLoading : Change("network -> loading expansions")
            class Error(val description: String) : Change("error -> $description")
            class ExpansionsLoaded(val expansions: List<Expansion>) : Change("network -> expansions(${expansions.size}) loaded")
        }


        companion object {
            @JvmField val CREATOR = PaperParcelBrowseUi_State.CREATOR

            val DEFAULT by lazy {
                State(false, null, emptyList())
            }
        }
    }

}