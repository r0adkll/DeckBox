package com.r0adkll.deckbuilder.arch.ui.features.testing


import com.r0adkll.deckbuilder.arch.ui.components.BaseActions
import com.r0adkll.deckbuilder.arch.ui.components.renderers.StateRenderer
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


interface DeckTestingUi : StateRenderer<DeckTestingUi.State> {

    val state: State


    interface Intentions {

    }


    interface Actions : BaseActions {

    }


    @PaperParcel
    data class State(
            val isLoading: Boolean,
            val error: String?
    ) : PaperParcelable {

        fun reduce(change: Change): State = when(change) {
            Change.IsLoading -> this.copy(isLoading = true, error = null)
            is Change.Error -> this.copy(error = change.description, isLoading = false)
        }


        sealed class Change(val logText: String) {
            object IsLoading : Change("cache -> loading deck")
            class Error(val description: String) : Change("error -> $description")
        }


        companion object {
            @JvmField val CREATOR = PaperParcelDeckTestingUi_State.CREATOR

            val DEFAULT by lazy {
                State(false, null)
            }
        }
    }
}