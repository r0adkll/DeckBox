package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder

import com.ftinc.kit.arch.presentation.state.Ui
import com.r0adkll.deckbuilder.arch.domain.Format
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.collection.model.CollectionCount
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Session
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Product
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Validation
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.util.stack
import io.pokemontcg.model.SuperType.ENERGY
import io.pokemontcg.model.SuperType.POKEMON
import io.pokemontcg.model.SuperType.TRAINER
import io.pokemontcg.model.SuperType.UNKNOWN
import io.reactivex.Observable
import paperparcel.PaperParcel
import paperparcel.PaperParcelable

interface DeckBuilderUi : Ui<DeckBuilderUi.State, DeckBuilderUi.State.Change> {

    interface Intentions {

        fun saveDeck(): Observable<Unit>
        fun addCards(): Observable<List<PokemonCard>>
        fun removeCard(): Observable<PokemonCard>
        fun editDeckClicks(): Observable<Boolean>
        fun editOverviewClicks(): Observable<Boolean>
        fun editDeckName(): Observable<String>
        fun editDeckDescription(): Observable<String>
        fun editDeckCollectionOnly(): Observable<Boolean>
    }

    interface Actions {

        fun showBrokenRules(errors: List<Int>)
        fun showFormat(format: Format)
        fun showIsSaving(isSaving: Boolean)
        fun showIsEditing(isEditing: Boolean)
        fun showIsOverview(isOverview: Boolean)
        fun showError(description: String)
        fun showSaveAction(hasChanges: Boolean)
        fun showCardCount(count: Int)
        fun showPokemonCards(cards: List<StackedPokemonCard>)
        fun showTrainerCards(cards: List<StackedPokemonCard>)
        fun showEnergyCards(cards: List<StackedPokemonCard>)
        fun showDeckName(name: String)
        fun showDeckDescription(description: String)
        fun showDeckImage(image: DeckImage?)
        fun showDeckCollectionOnly(collectionOnly: Boolean)
        fun showPrices(low: Double?, market: Double?, high: Double?)
        fun showCollectionPrices(low: Double?, market: Double?, high: Double?)
    }

    @PaperParcel
    data class State @JvmOverloads constructor(
            val sessionId: Long,

            val isSaving: Boolean,
            val isEditing: Boolean,
            val isChanged: Boolean,
            val isOverview: Boolean,
            val error: String?,

            val name: String?,
            val description: String?,
            val image: DeckImage?,
            val collectionOnly: Boolean,

            val validation: Validation,
            val products: Map<String, Product>,

            @Transient val pokemonCards: List<PokemonCard> = emptyList(),
            @Transient val trainerCards: List<PokemonCard> = emptyList(),
            @Transient val energyCards: List<PokemonCard> = emptyList(),
            @Transient val collectionCounts: List<CollectionCount> = emptyList()
    ) : Ui.State<State.Change>, PaperParcelable {

        val allCards: List<PokemonCard>
            get() = pokemonCards.plus(trainerCards).plus(energyCards)

        val allCardsStackedWithCollection: List<StackedPokemonCard>
            get() = allCards.stack(collectionCounts)

        override fun reduce(change: Change): State = when(change) {
            Change.Saving -> copy(isSaving = true, error = null)
            Change.Saved -> copy(isSaving = false)
            is Change.SessionUpdated -> copy(
                    pokemonCards = change.session.cards.filter { it.supertype == POKEMON },
                    trainerCards = change.session.cards.filter { it.supertype == TRAINER },
                    energyCards = change.session.cards.filter { it.supertype == ENERGY },
                    name = change.session.name,
                    description = change.session.description,
                    image = change.session.image,
                    collectionOnly = change.session.collectionOnly,
                    isChanged = change.session.hasChanges,
                    isSaving = false,
                    error = null
            )
            is Change.Editing -> copy(isEditing = change.isEditing)
            is Change.Overview -> copy(isOverview = change.isOverview)
            is Change.Error -> copy(error = change.description)
            is Change.AddCards -> {
                val pokemons = change.cards.filter { it.supertype == POKEMON }
                val trainers = change.cards.filter { it.supertype == TRAINER }
                val energies = change.cards.filter { it.supertype == ENERGY }
                copy(pokemonCards = pokemonCards.plus(pokemons),
                        trainerCards = trainerCards.plus(trainers),
                        energyCards = energyCards.plus(energies))
            }
            is Change.RemoveCard -> when(change.card.supertype) {
                POKEMON -> copy(pokemonCards = pokemonCards.minus(change.card))
                TRAINER -> copy(trainerCards = trainerCards.minus(change.card))
                ENERGY -> copy(energyCards = energyCards.minus(change.card))
                UNKNOWN -> this
            }
            is Change.EditCards -> copy(
                    pokemonCards = change.cards.filter { it.supertype == POKEMON },
                    trainerCards = change.cards.filter { it.supertype == TRAINER },
                    energyCards = change.cards.filter { it.supertype == ENERGY })
            is Change.EditName -> copy(name = change.name)
            is Change.EditDescription -> copy(description = change.description)
            is Change.Validated -> copy(validation = change.validation)
            is Change.CollectionCounts -> copy(collectionCounts = change.counts)
            is Change.PriceProducts -> copy(products = change.products)
        }

        override fun toString(): String {
            return "State(sessionId=$sessionId, " +
                    "isSaving=$isSaving, " +
                    "isEditing=$isEditing, " +
                    "isChanged=$isChanged, " +
                    "isOverview=$isOverview, " +
                    "error=$error, " +
                    "pokemonCards=${pokemonCards.size}, " +
                    "trainerCards=${trainerCards.size}, " +
                    "energyCards=${energyCards.size}, " +
                    "name=$name, " +
                    "description=$description, " +
                    "image=$image, " +
                    "validation=$validation, " +
                    "collection=${collectionCounts.size}, " +
                    "products=${products.size})"
        }

        sealed class Change(logText: String) : Ui.State.Change(logText) {
            object Saving : Change("user -> is saving deck")
            object Saved : Change("network -> deck saved!")
            class SessionUpdated(val session: Session) : Change("cache -> Session changed/updated $session")
            class Editing(val isEditing: Boolean) : Change("user -> is editing: $isEditing")
            class Overview(val isOverview: Boolean) : Change("user -> is overview: $isOverview")
            class Error(val description: String) : Change("error -> $description")
            class AddCards(val cards: List<PokemonCard>) : Change("user -> added ${cards.size} cards")
            class RemoveCard(val card: PokemonCard) : Change("user -> removing ${card.name}")
            class EditCards(val cards: List<PokemonCard>) : Change("user -> edited all cards: ${cards.size}")
            class EditName(val name: String) : Change("user -> name changed $name")
            class EditDescription(val description: String) : Change ("user -> desc changed $description")
            class Validated(val validation: Validation) : Change("cache -> validated: $validation")
            class CollectionCounts(val counts: List<CollectionCount>) : Change("cache -> collection count loaded/changed: ${counts.size}")
            class PriceProducts(val products: Map<String, Product>) : Change("cache -> price products loaded: ${products.size}")
        }

        companion object {
            @JvmField val CREATOR = PaperParcelDeckBuilderUi_State.CREATOR

            val DEFAULT by lazy {
                State(
                        sessionId = -1L,
                        isSaving = false,
                        isEditing = false,
                        isChanged = false,
                        isOverview = false,
                        error = null,
                        name = null,
                        description = null,
                        image = null,
                        collectionOnly = false,
                        validation = Validation(
                                standard = false,
                                expanded = false,
                                rules = emptyList()
                        ),
                        products = emptyMap(),
                        pokemonCards = emptyList(),
                        trainerCards = emptyList(),
                        energyCards = emptyList()
                )
            }
        }
    }
}
