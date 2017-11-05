package com.r0adkll.deckbuilder.arch.ui.features.carddetail

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.renderers.StateRenderer
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


interface CardDetailUi : StateRenderer<CardDetailUi.State> {

    val state: State


    interface Actions {

        fun showVariants(cards: List<PokemonCard>)
        fun showEvolvesFrom(cards: List<PokemonCard>)
    }


    @PaperParcel
    data class State(
            val card: PokemonCard?,
            val variants: List<PokemonCard>,
            val evolvesFrom: List<PokemonCard>
    ) : PaperParcelable {

        fun reduce(change: Change): State = when(change) {
            is Change.VariantsLoaded -> this.copy(variants = change.cards)
            is Change.EvolvesFromLoaded -> this.copy(evolvesFrom = change.cards)
        }


        sealed class Change(val logText: String) {
            class VariantsLoaded(val cards: List<PokemonCard>) : Change("network -> variants loaded")
            class EvolvesFromLoaded(val cards: List<PokemonCard>) : Change("network -> evolves loaded")
        }

        companion object {
            @JvmField val CREATOR = PaperParcelCardDetailUi_State.CREATOR
        }
    }

}