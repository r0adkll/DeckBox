package com.r0adkll.deckbuilder.arch.ui.features.testing


import com.ftinc.kit.arch.presentation.BaseActions
import com.ftinc.kit.arch.presentation.state.BaseState
import com.ftinc.kit.arch.presentation.state.Ui
import com.r0adkll.deckbuilder.arch.domain.features.testing.TestResults
import io.reactivex.Observable
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


interface DeckTestingUi : Ui<DeckTestingUi.State, DeckTestingUi.State.Change> {


    interface Intentions {

        fun runTests(): Observable<Int>
    }


    interface Actions : BaseActions {

    }


    @PaperParcel
    data class State(
            override val isLoading: Boolean,
            override val error: String?,
            val sessionId: Long,
            val results: TestResults?
    ) : BaseState<State.Change>(isLoading, error), PaperParcelable {

        override fun reduce(change: Change): Ui.State<Change> = when(change) {
            Change.IsLoading -> this.copy(isLoading = true, error = null)
            is Change.Error -> this.copy(error = change.description, isLoading = false)
            is Change.Results -> this.copy(results = change.results, isLoading = false)
        }


        sealed class Change(logText: String) : Ui.State.Change(logText) {
            object IsLoading : Change("cache -> loading deck")
            class Error(val description: String) : Change("error -> $description")
            class Results(val results: TestResults) : Change("test -> $results")
        }


        companion object {
            @JvmField val CREATOR = PaperParcelDeckTestingUi_State.CREATOR

            val DEFAULT by lazy {
                State(false, null, -1L, null)
            }
        }
    }
}