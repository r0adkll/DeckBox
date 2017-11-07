package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.components.renderers.StateRenderer
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


interface DeckBuilderUi : StateRenderer<DeckBuilderUi.State>{

    val state: State


    interface Intentions {

        fun saveDeck(): Observable<Unit>
        fun addCards(): Observable<List<PokemonCard>>
        fun removeCard(): Observable<PokemonCard>
        fun editDeckName(): Observable<String>
        fun editDeckDescription(): Observable<String>
    }


    interface Actions {

        fun showIsStandard(isStandard: Boolean)
        fun showIsExpanded(isExpanded: Boolean)
        fun showIsSaving(isSaving: Boolean)
        fun showSaveAction(hasChanges: Boolean)
        fun showCardCount(count: Int)
        fun showPokemonCards(cards: List<StackedPokemonCard>)
        fun showTrainerCards(cards: List<StackedPokemonCard>)
        fun showEnergyCards(cards: List<StackedPokemonCard>)
        fun showDeckName(name: String)
        fun showDeckDescription(description: String)
    }


    @PaperParcel
    data class State(
            val isSaving: Boolean,
            val deck: Deck?,
            val pokemonCards: List<PokemonCard>,
            val trainerCards: List<PokemonCard>,
            val energyCards: List<PokemonCard>,
            val name: String?,
            val description: String?
    ) : PaperParcelable {

        val isStandardLegal: Boolean
            get() {
                val poke = pokemonCards.find { it.expansion?.standardLegal == false } == null
                val trainer = trainerCards.find { it.expansion?.standardLegal == false } == null
                val energy = energyCards.filter { it.subtype != SubType.BASIC }
                        .find { it.expansion?.standardLegal == false } == null
                return poke && trainer && energy
            }

        val isExpandedLegal: Boolean
            get() {
                val poke = pokemonCards.find { it.expansion?.expandedLegal == false } == null
                val trainer = trainerCards.find { it.expansion?.expandedLegal == false } == null
                val energy = energyCards.filter { it.subtype != SubType.BASIC }
                        .find { it.expansion?.expandedLegal == false } == null
                return poke && trainer && energy
            }

        val hasChanged: Boolean
            get() {
                return if (deck == null) {
                    pokemonCards.isNotEmpty() ||
                            trainerCards.isNotEmpty() ||
                            energyCards.isNotEmpty() ||
                            !name.isNullOrBlank() ||
                            !description.isNullOrBlank()
                } else {
                    !deck.cards.containsAll(allCards) ||
                            deck.cards.size != allCards.size ||
                            deck.name != name ||
                            deck.description != description
                }
            }

        val allCards: List<PokemonCard>
            get() = pokemonCards.plus(trainerCards).plus(energyCards)


        fun reduce(change: Change): State = when(change) {
            Change.Saving -> this.copy(isSaving = true)
            is Change.AddCards -> {
                val pokemons = change.cards.filter { it.supertype == SuperType.POKEMON }
                val trainers = change.cards.filter { it.supertype == SuperType.TRAINER }
                val energies = change.cards.filter { it.supertype == SuperType.ENERGY }
                this.copy(pokemonCards = pokemonCards.plus(pokemons),
                        trainerCards = trainerCards.plus(trainers),
                        energyCards = energyCards.plus(energies))
            }
            is Change.RemoveCard -> when(change.card.supertype) {
                SuperType.POKEMON -> this.copy(pokemonCards = pokemonCards.minus(change.card))
                SuperType.TRAINER -> this.copy(trainerCards = trainerCards.minus(change.card))
                SuperType.ENERGY -> this.copy(energyCards = energyCards.minus(change.card))
                SuperType.UNKNOWN -> this
            }
            is Change.EditName -> this.copy(name = change.name)
            is Change.EditDescription -> this.copy(description = change.description)
            is Change.DeckUpdated -> this.copy(deck = change.deck, isSaving = false)
        }


        sealed class Change(val logText: String) {
            object Saving : Change("user -> is saving deck")
            class AddCards(val cards: List<PokemonCard>) : Change("user -> added ${cards.size} cards")
            class RemoveCard(val card: PokemonCard) : Change("user -> removing ${card.name}")
            class EditName(val name: String) : Change("user -> name changed $name")
            class EditDescription(val description: String) : Change ("user -> desc changed $description")
            class DeckUpdated(val deck: Deck) : Change("cache -> Deck changed/updated $deck")
        }

        companion object {
            @JvmField val CREATOR = PaperParcelDeckBuilderUi_State.CREATOR

            val DEFAULT by lazy {
                State(false, null, emptyList(), emptyList(), emptyList(), null, null)
            }
        }
    }
}