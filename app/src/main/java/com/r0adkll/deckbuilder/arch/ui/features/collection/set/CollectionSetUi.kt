package com.r0adkll.deckbuilder.arch.ui.features.collection.set

import android.os.Parcelable
import com.ftinc.kit.arch.presentation.BaseActions
import com.ftinc.kit.arch.presentation.state.BaseState
import com.ftinc.kit.arch.presentation.state.Ui
import com.r0adkll.deckbuilder.arch.ui.features.collection.set.CollectionSetUi.State
import com.r0adkll.deckbuilder.arch.ui.features.collection.set.CollectionSetUi.State.Change
import kotlinx.android.parcel.Parcelize


interface CollectionSetUi : Ui<State, Change> {

    interface Intentions {

    }

    interface Actions : BaseActions {

    }

    @Parcelize
    data class State(
            override var isLoading: Boolean,
            override var error: String?
    ): BaseState<Change>(isLoading, error), Parcelable {

        override fun reduce(change: Change): Ui.State<Change> = when(change) {
            Change.IsLoading -> copy(isLoading = true, error = null)
            is Change.Error -> copy(error = change.description, isLoading = false)
        }

        sealed class Change(logText: String): Ui.State.Change(logText) {
            object IsLoading : Change("user -> loading started")
            class Error(val description: String) : Change("error -> $description")
        }
    }
}