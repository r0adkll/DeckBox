package com.r0adkll.deckbuilder.arch.ui.features.settings.cache

import com.ftinc.kit.arch.presentation.BaseActions
import com.ftinc.kit.arch.presentation.state.BaseState
import com.ftinc.kit.arch.presentation.state.Ui

interface ManageCacheUi : Ui<ManageCacheUi.State, ManageCacheUi.State.Change> {

    interface Intentions {

    }

    interface Actions : BaseActions {

    }

    data class State(
        override val isLoading: Boolean,
        override val error: String?
    ) : BaseState<State.Change>(isLoading, error) {

        override fun reduce(change: Change): Ui.State<Change> = when (change) {
            Change.IsLoading -> copy(isLoading = true, error = null)
            is Change.Error -> copy(error = change.description, isLoading = false)
        }

        sealed class Change(logText: String) : Ui.State.Change(logText) {
            object IsLoading : Change("disk -> Loading cache statuses")
            class Error(val description: String) : Change("error -> $description")
        }
    }
}
