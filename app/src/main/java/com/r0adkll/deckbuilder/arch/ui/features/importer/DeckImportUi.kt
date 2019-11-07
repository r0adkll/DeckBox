package com.r0adkll.deckbuilder.arch.ui.features.importer

import android.os.Parcelable
import com.ftinc.kit.arch.presentation.BaseActions
import com.ftinc.kit.arch.presentation.state.BaseState
import com.ftinc.kit.arch.presentation.state.Ui
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize

interface DeckImportUi : Ui<DeckImportUi.State, DeckImportUi.State.Change> {

    interface Intentions {

        fun importDeckList(): Observable<String>
    }

    interface Actions : BaseActions {

        fun setResults(cards: List<PokemonCard>)
    }

    @Parcelize
    data class State(
        override val isLoading: Boolean,
        override val error: String?,
        val cards: List<PokemonCard>
    ) : BaseState<State.Change>(isLoading, error), Parcelable {

        override fun reduce(change: Change): State = when (change) {
            Change.IsLoading -> this.copy(isLoading = true)
            is Change.Error -> this.copy(error = change.description, isLoading = false)
            is Change.DeckListConverted -> this.copy(cards = change.cards, error = null, isLoading = false)
        }

        sealed class Change(logText: String) : Ui.State.Change(logText) {
            object IsLoading : Change("network -> starting conversion on deck list")
            class Error(val description: String) : Change("error -> $description")
            class DeckListConverted(
                val cards: List<PokemonCard>
            ) : Change("network -> decklist converted ${cards.size} cards")
        }

        override fun toString(): String {
            return "State(isLoading=$isLoading, error=$error, cards=${cards.size})"
        }

        companion object {

            val DEFAULT by lazy {
                State(false, null, emptyList())
            }
        }
    }
}
