package com.r0adkll.deckbuilder.arch.ui.features.browser


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.OfflineStatus
import com.r0adkll.deckbuilder.arch.ui.components.BaseActions
import com.r0adkll.deckbuilder.arch.ui.components.renderers.StateRenderer
import com.r0adkll.deckbuilder.arch.ui.features.browser.adapter.Item
import io.reactivex.Observable
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


interface BrowseUi : StateRenderer<BrowseUi.State> {

    val state: State


    interface Intentions {

        fun refreshExpansions(): Observable<Unit>
        fun downloadExpansion(): Observable<Expansion>
        fun downloadFormatExpansions(): Observable<List<Expansion>>
    }


    interface Actions : BaseActions {

        fun setExpansionsItems(items: List<Item>)
    }


    @PaperParcel
    data class State(
            val isLoading: Boolean,
            val error: String?,
            val expansions: List<Expansion>,
            val offlineStatus: OfflineStatus?
    ) : PaperParcelable {

        fun reduce(change: Change): State = when(change) {
            Change.IsLoading -> this.copy(isLoading = true, error = null)
            is Change.Error -> this.copy(error = change.description, isLoading = false)
            is Change.ExpansionsLoaded -> this.copy(expansions = change.expansions, isLoading = false)
            is Change.OfflineStatusUpdated -> this.copy(offlineStatus = change.status)
        }


        sealed class Change(val logText: String) {
            object IsLoading : Change("network -> loading expansions")
            class Error(val description: String) : Change("error -> $description")
            class ExpansionsLoaded(val expansions: List<Expansion>) : Change("network -> expansions(${expansions.size}) loaded")
            class OfflineStatusUpdated(val status: OfflineStatus) : Change("disk -> offline status($status)")
        }


        override fun toString(): String {
            return "State(isLoading=$isLoading, error=$error, expansions=${expansions.map { it.code }}, offlineStats=$offlineStatus)"
        }


        companion object {
            @JvmField val CREATOR = PaperParcelBrowseUi_State.CREATOR

            val DEFAULT by lazy {
                State(false, null, emptyList(), null)
            }
        }
    }

}