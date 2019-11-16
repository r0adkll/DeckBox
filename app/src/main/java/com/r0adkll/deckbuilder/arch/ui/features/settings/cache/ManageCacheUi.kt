package com.r0adkll.deckbuilder.arch.ui.features.settings.cache

import com.ftinc.kit.arch.presentation.BaseActions
import com.ftinc.kit.arch.presentation.state.BaseState
import com.ftinc.kit.arch.presentation.state.Ui
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.OfflineStatus
import com.r0adkll.deckbuilder.arch.ui.features.settings.cache.adapter.ExpansionCache
import io.reactivex.Observable

interface ManageCacheUi : Ui<ManageCacheUi.State, ManageCacheUi.State.Change> {

    interface Intentions {

        fun deleteCache(): Observable<Expansion>
        fun deleteAllCache(): Observable<Unit>
    }

    interface Actions : BaseActions {

        fun setTotalSize(size: String, label: String)
        fun setItems(items: List<ExpansionCache>)
    }

    data class State(
        override val isLoading: Boolean,
        override val error: String?,
        val offlineStatus: OfflineStatus
    ) : BaseState<State.Change>(isLoading, error) {

        override fun reduce(change: Change): Ui.State<Change> = when (change) {
            Change.IsLoading -> copy(isLoading = true, error = null)
            is Change.Error -> copy(error = change.description, isLoading = false)
            is Change.CacheStatus -> copy(offlineStatus = change.status, isLoading = false)
        }

        sealed class Change(logText: String) : Ui.State.Change(logText) {
            object IsLoading : Change("disk -> Loading cache statuses")
            class Error(val description: String) : Change("error -> $description")
            class CacheStatus(val status: OfflineStatus) : Change("offline -> $status")
        }

        companion object {

            val DEFAULT by lazy {
                State(true, null, OfflineStatus())
            }
        }
    }
}
