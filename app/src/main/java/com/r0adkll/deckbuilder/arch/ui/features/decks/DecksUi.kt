package com.r0adkll.deckbuilder.arch.ui.features.decks

import android.os.Parcelable
import com.ftinc.kit.arch.presentation.BaseActions
import com.ftinc.kit.arch.presentation.state.BaseState
import com.ftinc.kit.arch.presentation.state.Ui
import com.r0adkll.deckbuilder.arch.domain.features.community.model.DeckTemplate
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.ValidatedDeck
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.ExpansionPreview
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.Item
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize

interface DecksUi : Ui<DecksUi.State, DecksUi.State.Change> {

    interface Intentions {

        fun shareClicks(): Observable<Deck>
        fun duplicateClicks(): Observable<Deck>
        fun deleteClicks(): Observable<Deck>
        fun dismissPreview(): Observable<Unit>
    }

    interface Actions : BaseActions {

        fun showItems(items: List<Item>)
        fun balanceShortcuts(decks: List<Deck>)
    }

    @Parcelize
    data class QuickStart(
        val templates: List<DeckTemplate> = emptyList()
    ) : Parcelable

    @Parcelize
    data class State(
        override val isLoading: Boolean,
        override val error: String?,
        val hasLoadedOnce: Boolean,
        val decks: List<ValidatedDeck>,
        val preview: ExpansionPreview?,
        val quickStart: QuickStart?
    ) : BaseState<State.Change>(isLoading, error), Parcelable {

        @Suppress("ComplexMethod")
        override fun reduce(change: Change): State = when (change) {
            Change.IsLoading -> this.copy(isLoading = true, error = null)
            Change.DeckDeleted -> this
            Change.HidePreview -> this.copy(preview = null)
            Change.HideQuickStart -> this.copy(quickStart = null)
            is Change.ShowPreview -> this.copy(preview = change.preview)
            is Change.ShowQuickStart -> this.copy(quickStart = change.quickStart)
            is Change.Error -> this.copy(error = change.description, isLoading = false)
            is Change.DecksLoaded -> this.copy(
                decks = change.decks,
                isLoading = false,
                hasLoadedOnce = true,
                error = null
            )
        }

        sealed class Change(logText: String) : Ui.State.Change(logText) {
            object IsLoading : Change("network -> loading decks")
            class Error(val description: String) : Change("error -> $description")
            class DecksLoaded(val decks: List<ValidatedDeck>) : Change("network -> decks loaded ${decks.size}")
            class ShowPreview(
                val preview: ExpansionPreview
            ) : Change("user -> show preview (version: ${preview.version}, expansion: ${preview.code})")
            class ShowQuickStart(
                val quickStart: QuickStart
            ) : Change("network -> show quickstart templates: ${quickStart.templates.size}")
            object HideQuickStart : Change("user -> hide quickstart")
            object HidePreview : Change("user -> hide preview")
            object DeckDeleted : Change("user -> deck deleted")
        }

        override fun toString(): String {
            return "State(isLoading=$isLoading, error=$error, decks=${decks.size}, showPreview=${preview != null}, " +
                "showQuickstart=${quickStart != null})"
        }

        companion object {

            val DEFAULT by lazy {
                State(false, null, false, emptyList(), null, null)
            }
        }
    }
}
