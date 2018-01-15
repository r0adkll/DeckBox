package com.r0adkll.deckbuilder.arch.ui.features.carddetail

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Validation
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
            val card: PokemonCard?,
            val deck: Deck?,
            val variants: List<PokemonCard>,
            val evolvesFrom: List<PokemonCard>,
            val validation: Validation
    ) : PaperParcelable {

        val hasCopies: Boolean
            get() = deck?.cards?.filter { it.id == card?.id }.orEmpty().isNotEmpty()


        fun reduce(change: Change): State = when(change) {
            Change.AddCard -> {
                if (deck != null && card != null) {
                    val updatedDeck = deck.copy(cards = deck.cards.plus(card))
                    this.copy(deck = updatedDeck)
                } else {
                    this
                }
            }
            Change.RemoveCard -> {
                if (deck != null && card != null) {
                    val updatedDeck = deck.copy(cards = deck.cards.minus(card))
                    this.copy(deck = updatedDeck)
                } else {
                    this
                }
            }
            is Change.Validated -> this.copy(validation = change.validation)
            is Change.VariantsLoaded -> this.copy(variants = change.cards)
            is Change.EvolvesFromLoaded -> this.copy(evolvesFrom = change.cards)
        }


        sealed class Change(val logText: String) {
            object AddCard : Change("user -> adding another copy")
            object RemoveCard : Change("user -> removing copy")
            class VariantsLoaded(val cards: List<PokemonCard>) : Change("network -> variants loaded")
            class EvolvesFromLoaded(val cards: List<PokemonCard>) : Change("network -> evolves loaded")
            class Validated(val validation: Validation) : Change("network -> card validated: $validation")
        }

        companion object {
            @JvmField val CREATOR = PaperParcelCardDetailUi_State.CREATOR

            val DEFAULT by lazy {
                CardDetailUi.State(null, null, emptyList(), emptyList(), Validation(false, false))
            }
        }
    }

}