package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder


import com.r0adkll.deckbuilder.arch.domain.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.renderers.StateRenderer
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


interface DeckBuilderUi : StateRenderer<DeckBuilderUi.State>{

    val state: State


    interface Intentions {

        fun addCard(): Observable<PokemonCard>
        fun removeCard(): Observable<PokemonCard>
        fun editDeckName(): Observable<String>
        fun editDeckDescription(): Observable<String>
    }


    interface Actions {

        fun showPokemonCards(cards: List<PokemonCard>)
        fun showTrainerCards(cards: List<PokemonCard>)
        fun showEnergyCards(cards: List<PokemonCard>)
        fun showDeckName(name: String)
        fun showDeckDescription(description: String)
    }


    @PaperParcel
    data class State(
            val pokemonCards: List<PokemonCard>,
            val trainerCards: List<PokemonCard>,
            val energyCards: List<PokemonCard>,
            val name: String?,
            val description: String?
    ) : PaperParcelable {

        fun reduce(change: Change): State = when(change) {
            is Change.AddCard -> when(change.card.supertype) {
                SuperType.POKEMON -> this.copy(pokemonCards = pokemonCards.plus(change.card))
                SuperType.TRAINER -> this.copy(trainerCards = trainerCards.plus(change.card))
                SuperType.ENERGY -> this.copy(energyCards = energyCards.plus(change.card))
                SuperType.UNKNOWN -> this
            }
            is Change.RemoveCard -> when(change.card.supertype) {
                SuperType.POKEMON -> this.copy(pokemonCards = pokemonCards.minus(change.card))
                SuperType.TRAINER -> this.copy(trainerCards = trainerCards.minus(change.card))
                SuperType.ENERGY -> this.copy(energyCards = energyCards.minus(change.card))
                SuperType.UNKNOWN -> this
            }
            is Change.EditName -> this.copy(name = change.name)
            is Change.EditDescription -> this.copy(description = change.description)
        }


        sealed class Change(val logText: String) {
            class AddCard(val card: PokemonCard) : Change("user -> added ${card.name}")
            class RemoveCard(val card: PokemonCard) : Change("user -> removing ${card.name}")
            class EditName(val name: String) : Change("user -> name changed $name")
            class EditDescription(val description: String) : Change ("user -> desc changed $description")
        }

        companion object {
            @JvmField val CREATOR = PaperParcelDeckBuilderUi_State.CREATOR
        }
    }
}