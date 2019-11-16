package com.r0adkll.deckbuilder.arch.ui.features.browser

import android.os.Parcelable
import com.ftinc.kit.arch.presentation.BaseActions
import com.ftinc.kit.arch.presentation.state.BaseState
import com.ftinc.kit.arch.presentation.state.Ui
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.OfflineStatus
import com.r0adkll.deckbuilder.arch.ui.features.browser.adapter.Item
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize

interface BrowseUi : Ui<BrowseUi.State, BrowseUi.State.Change> {

    interface Intentions {

        fun refreshExpansions(): Observable<Unit>
        fun downloadExpansion(): Observable<Expansion>
        fun downloadFormatExpansions(): Observable<List<Expansion>>
        fun hideOfflineOutline(): Observable<Unit>
    }

    interface Actions : BaseActions {

        fun setExpansionsItems(items: List<Item>)
    }

    @Parcelize
    data class State(
        override val isLoading: Boolean,
        override val error: String?,
        val expansions: List<Expansion>,
        val offlineStatus: OfflineStatus?,
        val offlineOutline: Boolean
    ) : BaseState<State.Change>(isLoading, error), Parcelable {

        override fun reduce(change: Change): State = when (change) {
            Change.IsLoading -> this.copy(isLoading = true, error = null)
            is Change.Error -> this.copy(error = change.description, isLoading = false)
            is Change.ExpansionsLoaded -> this.copy(expansions = change.expansions, isLoading = false)
            is Change.OfflineStatusUpdated -> this.copy(offlineStatus = change.status)
            is Change.OfflineOutline -> this.copy(offlineOutline = change.enabled)
        }

        sealed class Change(logText: String) : Ui.State.Change(logText) {
            object IsLoading : Change("network -> loading expansions")
            class Error(val description: String) : Change("error -> $description")
            class ExpansionsLoaded(
                val expansions: List<Expansion>
            ) : Change("network -> expansions(${expansions.size}) loaded")
            class OfflineStatusUpdated(val status: OfflineStatus) : Change("disk -> offline status($status)")
            class OfflineOutline(val enabled: Boolean) : Change("user -> offline outline($enabled)")
        }

        override fun toString(): String {
            return "State(isLoading=$isLoading, error=$error, expansions=${expansions.size}, " +
                "offlineStats=${offlineStatus?.expansions?.size}, offlineOutline=$offlineOutline)"
        }

        companion object {

            val DEFAULT by lazy {
                State(false, null, emptyList(), null, true)
            }
        }
    }
}
