package com.r0adkll.deckbuilder.arch.ui.features.testing


import com.ftinc.kit.arch.presentation.BaseActions
import com.ftinc.kit.arch.presentation.state.BaseState
import com.ftinc.kit.arch.presentation.state.Ui
import com.r0adkll.deckbuilder.arch.domain.features.testing.TestResults
import com.r0adkll.deckbuilder.arch.ui.features.testing.adapter.TestResult
import io.reactivex.Observable
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


interface DeckTestingUi : Ui<DeckTestingUi.State, DeckTestingUi.State.Change> {


    interface Intentions {

        fun runTests(): Observable<Int>
        fun incrementIterations(): Observable<Int>
        fun decrementIterations(): Observable<Int>
    }


    interface Actions : BaseActions {

        fun showTestResults(results: List<TestResult>)
        fun setTestIterations(iterations: Int)
        fun setMetadata(metadata: Metadata)
    }


    @PaperParcel
    data class Metadata(
            val name: String,
            val description: String,
            val pokemon: Int,
            val trainer: Int,
            val energy: Int
    ): PaperParcelable {
        companion object {
            @JvmField val CREATOR = PaperParcelDeckTestingUi_Metadata.CREATOR
        }
    }


    @PaperParcel
    data class State(
            override val isLoading: Boolean,
            override val error: String?,

            val sessionId: Long?,
            val deckId: String?,
            val metadata: Metadata?,

            val iterations: Int,
            val results: TestResults?
    ) : BaseState<State.Change>(isLoading, error), PaperParcelable {

        override fun reduce(change: Change): Ui.State<Change> = when(change) {
            Change.IsLoading -> this.copy(isLoading = true, error = null)
            is Change.Error -> this.copy(error = change.description, isLoading = false)
            is Change.Results -> this.copy(results = change.results, isLoading = false)
            is Change.IncrementIterations -> this.copy(iterations = iterations.plus(change.amount).coerceAtLeast(0))
            is Change.DecrementIterations -> this.copy(iterations = iterations.minus(change.amount).coerceAtLeast(0))
            is Change.MetadataLoaded -> this.copy(metadata = change.metadata)
        }


        sealed class Change(logText: String) : Ui.State.Change(logText) {
            object IsLoading : Change("cache -> loading deck")
            class Error(val description: String) : Change("error -> $description")
            class Results(val results: TestResults) : Change("test -> $results")
            class IncrementIterations(val amount: Int) : Change("user -> increment iterations by $amount")
            class DecrementIterations(val amount: Int) : Change("user -> decrement iterations by $amount")
            class MetadataLoaded(val metadata: Metadata) : Change("network -> metadata loaded: $metadata")
        }


        companion object {
            @JvmField val CREATOR = PaperParcelDeckTestingUi_State.CREATOR

            val DEFAULT by lazy {
                State(false, null, null, null, null, 1000, null)
            }
        }
    }
}