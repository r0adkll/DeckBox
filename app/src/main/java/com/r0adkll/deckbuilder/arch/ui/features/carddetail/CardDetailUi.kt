package com.r0adkll.deckbuilder.arch.ui.features.carddetail

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Validation
import com.r0adkll.deckbuilder.arch.ui.components.renderers.StateRenderer
import io.reactivex.Observable
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


interface CardDetailUi : StateRenderer<CardDetailUi.State> {

    val state: State


    interface Intentions {

        fun addCardClicks(): Observable<Unit>
        fun removeCardClicks(): Observable<Unit>
    }


    interface Actions {

        fun showCopies(count: Int?)
        fun showVariants(cards: List<PokemonCard>)
        fun showEvolvesFrom(cards: List<PokemonCard>)
        fun showStandardValidation(isValid: Boolean)
        fun showExpandedValidation(isValid: Boolean)
    }


    @PaperParcel
    data class State(
            val sessionId: Long?,
            val card: PokemonCard?,
            val count: Int?,
            val variants: List<PokemonCard>,
            val evolvesFrom: List<PokemonCard>,
            val validation: Validation
    ) : PaperParcelable {

        val hasCopies: Boolean
            get() = count?.let { it > 0 } == true


        fun reduce(change: Change): State = when(change) {
            is Change.CountChanged -> this.copy(count = change.count)
            is Change.Validated -> this.copy(validation = change.validation)
            is Change.VariantsLoaded -> this.copy(variants = change.cards)
            is Change.EvolvesFromLoaded -> this.copy(evolvesFrom = change.cards)
        }


        sealed class Change(val logText: String) {
            class CountChanged(val count: Int) : Change("user -> number of copies changed $count")
            class VariantsLoaded(val cards: List<PokemonCard>) : Change("network -> variants loaded")
            class EvolvesFromLoaded(val cards: List<PokemonCard>) : Change("network -> evolves loaded")
            class Validated(val validation: Validation) : Change("network -> card validated: $validation")
        }


        override fun toString(): String {
            return "State(sessionId=$sessionId, card=${card?.id}, count=$count, variants=${variants.size}, evolvesFrom=${evolvesFrom.size}, validation=$validation"
        }


        companion object {
            @JvmField val CREATOR = PaperParcelCardDetailUi_State.CREATOR

            val DEFAULT by lazy {
                CardDetailUi.State(null, null, null, emptyList(), emptyList(), Validation(false, false, emptyList()))
            }
        }
    }

}