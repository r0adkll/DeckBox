package com.r0adkll.deckbuilder.arch.ui.features.decks


import android.os.Parcelable
import com.r0adkll.deckbuilder.arch.data.remote.model.ExpansionPreview
import com.r0adkll.deckbuilder.arch.domain.features.community.model.DeckTemplate
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.components.BaseActions
import com.r0adkll.deckbuilder.arch.ui.components.renderers.StateRenderer
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.Item
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize


interface DecksUi : StateRenderer<DecksUi.State> {

    val state: State


    interface Intentions {

        fun shareClicks(): Observable<Deck>
        fun duplicateClicks(): Observable<Deck>
        fun deleteClicks(): Observable<Deck>
        fun dismissPreview(): Observable<Unit>
        fun createSession(): Observable<Deck>
        fun createNewSession(): Observable<Unit>
        fun clearSession(): Observable<Unit>
    }


    interface Actions : BaseActions {

        fun showItems(items: List<Item>)
        fun balanceShortcuts(decks: List<Deck>)
        fun openSession(sessionId: Long)
    }


    @Parcelize
    data class QuickStart(
            val templates: List<DeckTemplate> = emptyList()
    ) : Parcelable


    @Parcelize
    data class State(
            val isLoading: Boolean,
            val error: String?,
            val decks: List<Deck>,
            val preview: ExpansionPreview?,
            val quickStart: QuickStart?,
            val isSessionLoading: String?,
            val sessionId: Long?
    ) : Parcelable {

        fun reduce(change: Change): State = when(change) {
            Change.IsLoading -> this.copy(isLoading = true, error = null)
            Change.DeckDeleted -> this
            Change.HidePreview -> this.copy(preview = null)
            Change.HideQuickStart -> this.copy(quickStart = null)
            Change.ClearSession -> this.copy(isSessionLoading = null, sessionId = null)
            is Change.ShowPreview -> this.copy(preview = change.preview)
            is Change.ShowQuickStart -> this.copy(quickStart = change.quickStart)
            is Change.Error -> this.copy(error = change.description, isLoading = false)
            is Change.DecksLoaded -> this.copy(decks = change.decks, isLoading = false, error = null)
            is Change.IsSessionLoading -> this.copy(isSessionLoading = change.deckId)
            is Change.SessionLoaded -> this.copy(sessionId = change.sessionId, isSessionLoading = null)
        }


        sealed class Change(val logText: String) {
            object IsLoading : Change("network -> loading decks")
            class Error(val description: String) : Change("error -> $description")
            class DecksLoaded(val decks: List<Deck>) : Change("network -> decks loaded ${decks.size}")
            class ShowPreview(val preview: ExpansionPreview) : Change("user -> show preview (version: ${preview.version}, expansion: ${preview.code})")
            class IsSessionLoading(val deckId: String) : Change("user -> creating session for ($deckId)")
            class SessionLoaded(val sessionId: Long) : Change("user -> session loaded $sessionId")
            class ShowQuickStart(val quickStart: DecksUi.QuickStart) : Change("network -> show quickstart templates: ${quickStart.templates.size}")
            object ClearSession : Change("self -> clearing session")
            object HideQuickStart : Change("user -> hide quickstart")
            object HidePreview : Change("user -> hide preview")
            object DeckDeleted : Change("user -> deck deleted")
        }


        override fun toString(): String {
            return "State(isLoading=$isLoading, error=$error, decks=${decks.size}, showPreview=${preview != null}, showQuickstart=${quickStart != null}, isSessionLoading=$isSessionLoading, sessionId=$sessionId)"
        }


        companion object {

            val DEFAULT by lazy {
                State(false, null, emptyList(), null, null, null, null)
            }
        }
    }
}