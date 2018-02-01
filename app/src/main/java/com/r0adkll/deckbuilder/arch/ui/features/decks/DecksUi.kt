package com.r0adkll.deckbuilder.arch.ui.features.decks


import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.components.BaseActions
import com.r0adkll.deckbuilder.arch.ui.components.renderers.StateRenderer
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.Item
import io.reactivex.Observable
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


interface DecksUi : StateRenderer<DecksUi.State> {

    val state: State


    interface Intentions {

        fun shareClicks(): Observable<Deck>
        fun duplicateClicks(): Observable<Deck>
        fun deleteClicks(): Observable<Deck>
        fun dismissPreview(): Observable<Unit>
    }


    interface Actions : BaseActions {

        fun showItems(items: List<Item>)
    }


    @PaperParcel
    data class State(
            val isLoading: Boolean,
            val error: String?,
            val decks: List<Deck>,
            val showPreview: Boolean
    ) : PaperParcelable {

        fun reduce(change: Change): State = when(change) {
            Change.IsLoading -> this.copy(isLoading = true, error = null)
            Change.DeckDeleted -> this
            is Change.ShowPreview -> this.copy(showPreview = change.showPreview)
            is Change.Error -> this.copy(error = change.description, isLoading = false)
            is Change.DecksLoaded -> this.copy(decks = change.decks, isLoading = false, error = null)
        }


        sealed class Change(val logText: String) {
            object IsLoading : Change("network -> loading decks")
            class Error(val description: String) : Change("error -> $description")
            class DecksLoaded(val decks: List<Deck>) : Change("network -> decks loaded ${decks.size}")
            class ShowPreview(val showPreview: Boolean) : Change("user -> show preview: $showPreview")
            object DeckDeleted : Change("user -> deck deleted")
        }


        override fun toString(): String {
            return "State(isLoading=$isLoading, error=$error, decks=${decks.size}, showPreview=$showPreview)"
        }


        companion object {
            @JvmField val CREATOR = PaperParcelDecksUi_State.CREATOR

            val DEFAULT by lazy {
                State(false, null, emptyList(), false)
            }
        }
    }
}