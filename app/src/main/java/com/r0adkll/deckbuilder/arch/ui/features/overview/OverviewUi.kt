package com.r0adkll.deckbuilder.arch.ui.features.overview

import com.ftinc.kit.arch.presentation.BaseActions
import com.ftinc.kit.arch.presentation.state.BaseState
import com.ftinc.kit.arch.presentation.state.Ui
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Session
import io.reactivex.Observable
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


interface OverviewUi : Ui<OverviewUi.State, OverviewUi.State.Change> {

    interface Intentions {

        fun addCards(): Observable<List<PokemonCard>>
        fun removeCard(): Observable<PokemonCard>
    }


    interface Actions : BaseActions {

        fun showCards(cards: List<EvolutionChain>)
    }


    @PaperParcel
    data class State @JvmOverloads constructor(
            override val isLoading: Boolean,
            override val error: String?,

            // The identifiers by which cards are loaded, these are set before 'start()' is called
            val sessionId: Long,

            @Transient val cards: List<PokemonCard> = emptyList()
    ) : BaseState<State.Change>(isLoading, error), PaperParcelable {

        override fun reduce(change: Change): Ui.State<Change> = when(change) {
            Change.IsLoading -> this.copy(isLoading = true, error = null)
            is Change.Error -> this.copy(error = change.description, isLoading = false)
            is Change.CardsLoaded -> this.copy(cards = change.cards, isLoading = false)
        }

        sealed class Change(logText: String) : Ui.State.Change(logText) {
            object IsLoading : Change("cache -> loading cards")
            class Error(val description: String) : Change("error -> $description")
            class CardsLoaded(val cards: List<PokemonCard>) : Change("network -> cards($cards.size) loaded")
        }

        override fun toString(): String {
            return "State(isLoading=$isLoading, error=$error, sessionId=$sessionId, cards=${cards.size})"
        }

        companion object {
            @JvmField val CREATOR = PaperParcelOverviewUi_State.CREATOR

            val DEFAULT by lazy {
                State(false, null, Session.NO_ID, emptyList())
            }
        }
    }
}