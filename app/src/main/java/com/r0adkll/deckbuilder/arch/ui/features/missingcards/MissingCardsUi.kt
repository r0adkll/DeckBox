package com.r0adkll.deckbuilder.arch.ui.features.missingcards


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.ui.components.BaseActions
import com.r0adkll.deckbuilder.arch.ui.components.renderers.StateRenderer
import io.reactivex.Observable
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


interface MissingCardsUi : StateRenderer<MissingCardsUi.State> {

    val state: State


    interface Intentions {

        fun editName(): Observable<String>
        fun editNumber(): Observable<Int>
        fun editDescription(): Observable<String>
        fun selectExpansion(): Observable<Expansion>
        fun selectPrint(): Observable<String>
        fun submitReport(): Observable<Unit>
    }


    interface Actions : BaseActions {

        fun setExpansions(expansions: List<Expansion>)
        fun setExpansion(expansion: Expansion?)
        fun setName(name: String?)
        fun setNumber(number: Int?)
        fun setDescription(description: String?)
        fun setPrint(print: String)
        fun setSendEnabled(enabled: Boolean)
        fun closeReport()
    }


    @PaperParcel
    data class State(
            val isLoading: Boolean,
            val error: String?,
            val expansions: List<Expansion>,
            val name: String?,
            val setNumber: Int?,
            val description: String?,
            val expansion: Expansion?,
            val print: String,
            val reportSubmitted: Boolean
    ) : PaperParcelable {

        val isReportReady: Boolean
            get() = !name.isNullOrBlank()

        fun reduce(change: Change): State = when(change) {
            Change.ReportSubmitted -> this.copy(reportSubmitted = true)
            Change.IsLoading -> this.copy(isLoading = true, error = null)
            is Change.Error -> this.copy(error = change.description, isLoading = false)
            is Change.ExpansionsLoaded -> this.copy(expansions = change.expansions)
            is Change.EditName -> this.copy(name = change.name)
            is Change.EditNumber -> this.copy(setNumber = if (change.number == -1) null else change.number)
            is Change.EditDescription -> this.copy(description = change.description)
            is Change.SelectedExpansion -> this.copy(expansion = change.expansion)
            is Change.SelectedPrint -> this.copy(print = change.print)
        }


        sealed class Change(val logText: String) {
            object IsLoading : Change("network -> submitting missing card request")
            object ReportSubmitted : Change("network -> report submitted to Firebase")
            class Error(val description: String) : Change("error -> $description")
            class ExpansionsLoaded(val expansions: List<Expansion>) : Change("network -> expansions(${expansions.size}) loaded")
            class EditName(val name: String) : Change("user -> name changed = $name")
            class EditNumber(val number: Int) : Change("user -> set number changed = $number")
            class EditDescription(val description: String) : Change("user -> description changed = $description")
            class SelectedExpansion(val expansion: Expansion?) : Change("user -> selected ${expansion?.name}")
            class SelectedPrint(val print: String) : Change("user -> selected $print")
        }

        companion object {
            @JvmField val CREATOR = PaperParcelMissingCardsUi_State.CREATOR

            val DEFAULT by lazy {
                State(false, null, emptyList(), null, null, null, null, "Regular Art", false)
            }
        }
    }

}