package com.r0adkll.deckbuilder.arch.ui.features.importer


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseActions
import com.r0adkll.deckbuilder.arch.ui.components.renderers.StateRenderer
import io.reactivex.Observable
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


interface DeckImportUi : StateRenderer<DeckImportUi.State> {

    val state: State


    interface Intentions {

        fun importDeckList(): Observable<String>
    }


    interface Actions : BaseActions {

        fun setResults(cards: List<PokemonCard>)
    }


    @PaperParcel
    data class State(
            val isLoading: Boolean,
            val error: String?,
            val cards: List<PokemonCard>
    ) : PaperParcelable {

        fun reduce(change: Change): State = when(change) {
            Change.IsLoading -> this.copy(isLoading = true)
            is Change.Error -> this.copy(error = change.description, isLoading = false)
            is Change.DeckListConverted -> this.copy(cards = change.cards, error = null, isLoading = false)
        }


        sealed class Change(val logText: String) {
            object IsLoading : Change("network -> starting conversion on deck list")
            class Error(val description: String) : Change("error -> $description")
            class DeckListConverted(val cards: List<PokemonCard>) : Change("network -> decklist converted ${cards.size} cards")
        }


        companion object {
            @JvmField val CREATOR = PaperParcelDeckImportUi_State.CREATOR

            val DEFAULT by lazy {
                State(false, null, emptyList())
            }
        }
    }
}