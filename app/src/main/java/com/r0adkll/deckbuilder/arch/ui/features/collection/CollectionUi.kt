package com.r0adkll.deckbuilder.arch.ui.features.collection

import android.os.Parcelable
import com.ftinc.kit.arch.presentation.BaseActions
import com.ftinc.kit.arch.presentation.state.BaseState
import com.ftinc.kit.arch.presentation.state.Ui
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.collection.model.CollectionCount
import com.r0adkll.deckbuilder.arch.ui.features.collection.adapter.Item
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize


interface CollectionUi : Ui<CollectionUi.State, CollectionUi.State.Change> {

    interface Intentions {

        fun migrateClicks(): Observable<Unit>
    }

    interface Actions : BaseActions {

        fun setItems(items: List<Item>)
    }

    @Parcelize
    data class State(
            override val isLoading: Boolean,
            override val error: String?,
            val isMigrationNeeded: Boolean,
            val isMigrationInProgress: Boolean,
            val migrationError: String?,

            val expansions: List<Expansion>,
            val counts: List<CollectionCount>
    ) : BaseState<State.Change>(isLoading, error), Parcelable {

        override fun reduce(change: Change): Ui.State<Change> = when(change) {
            Change.IsLoading -> copy(isLoading = true, error = null)
            is Change.Error -> copy(error = change.description, isLoading = false)
            is Change.Expansions -> copy(expansions = change.expansions, isLoading = false)
            is Change.Counts -> copy(counts = change.counts, isLoading = false)
            is Change.MigrationNeeded -> copy(isMigrationNeeded = change.isMigrationNeeded, isMigrationInProgress = if (!change.isMigrationNeeded) false else isMigrationInProgress)
            Change.IsMigrationInProgress -> copy(isMigrationInProgress = true)
            is Change.MigrationError -> copy(migrationError = change.description, isMigrationInProgress = false)
        }

        sealed class Change(logText: String): Ui.State.Change(logText) {
            object IsLoading : Change("network -> loading expansions")
            class Error(val description: String) : Change("error -> $description")
            class Expansions(val expansions: List<Expansion>) : Change("disk -> expansions loaded")
            class Counts(val counts: List<CollectionCount>) : Change("disk -> counts loaded")
            class MigrationNeeded(val isMigrationNeeded: Boolean) : Change("network -> Migration needed changes")
            object IsMigrationInProgress : Change("network -> migrating collection counts")
            class MigrationError(val description: String) : Change("error -> $description")
        }

        override fun toString(): String {
            return "CollectionUi.State(" +
                    "isLoading=$isLoading, " +
                    "error=$error, " +
                    "expansions=${expansions.size}, " +
                    "counts=${counts.size}, " +
                    "isMigrationNeeded=$isMigrationNeeded, " +
                    "isMigrationInProgress=$isMigrationInProgress, " +
                    "migrationError=$migrationError, " +
                    ")"
        }

        companion object {

            val DEFAULT by lazy {
                State(
                        true,
                        null,
                        false,
                        isMigrationInProgress = false,
                        migrationError = null,
                        expansions = emptyList(),
                        counts = emptyList()
                )
            }
        }
    }
}
