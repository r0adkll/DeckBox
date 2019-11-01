package com.r0adkll.deckbuilder.arch.ui.features.carddetail

import android.os.Parcelable
import com.ftinc.kit.arch.presentation.state.Ui
import com.r0adkll.deckbuilder.arch.domain.Format
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.marketplace.model.Product
import com.r0adkll.deckbuilder.arch.domain.features.validation.model.Validation
import io.reactivex.Observable
import kotlinx.android.parcel.Parcelize

interface CardDetailUi : Ui<CardDetailUi.State, CardDetailUi.State.Change> {

    interface Intentions {

        fun addCardClicks(): Observable<Unit>
        fun removeCardClicks(): Observable<Unit>

        fun incrementCollectionCount(): Observable<Unit>
        fun decrementCollectionCount(): Observable<Unit>
    }

    interface Actions {

        fun showCopies(count: Int?)
        fun showVariants(cards: List<PokemonCard>)
        fun showEvolvesFrom(cards: List<PokemonCard>)
        fun showEvolvesTo(cards: List<PokemonCard>)
        fun showValidation(format: Format)
        fun showCollectionCount(count: Int)
        fun hideCollectionCounter()
        fun showCardInformation(card: PokemonCard)
        fun showPrices(lowPrice: Double?, marketPrice: Double?, highPrice: Double?)
        fun showPriceHistory(products: List<Product>)
    }

    @Parcelize
    data class State(
        val sessionId: Long?,
        val card: PokemonCard?,
        val error: String?,
        val count: Int?,
        val variants: List<PokemonCard>,
        val evolvesFrom: List<PokemonCard>,
        val evolvesTo: List<PokemonCard>,
        val validation: Validation,
        val collectionCount: Int,
        val products: List<Product>?
    ) : Ui.State<State.Change>, Parcelable {

        val hasCopies: Boolean
            get() = count?.let { it > 0 } == true

        override fun reduce(change: Change): State = when (change) {
            is Change.Error -> this.copy(error = error)
            is Change.CountChanged -> this.copy(count = change.count)
            is Change.Validated -> this.copy(validation = change.validation)
            is Change.VariantsLoaded -> this.copy(variants = change.cards)
            is Change.EvolvesFromLoaded -> this.copy(evolvesFrom = change.cards)
            is Change.EvolvesToLoaded -> this.copy(evolvesTo = change.cards)
            is Change.CollectionCountChanged -> this.copy(collectionCount = change.count)
            is Change.CollectionCountUpdated -> this.copy(collectionCount = collectionCount + change.change)
            is Change.PriceUpdated -> this.copy(products = change.products)
        }

        sealed class Change(logText: String) : Ui.State.Change(logText) {
            class Error(val description: String) : Change("error -> $description")
            class CountChanged(val count: Int) : Change("user -> number of copies changed $count")
            class VariantsLoaded(val cards: List<PokemonCard>) : Change("network -> variants loaded")
            class EvolvesFromLoaded(val cards: List<PokemonCard>) : Change("network -> evolves from loaded")
            class EvolvesToLoaded(val cards: List<PokemonCard>) : Change("netowrk -> evolves to loaded")
            class Validated(val validation: Validation) : Change("network -> card validated: $validation")
            class CollectionCountChanged(val count: Int) : Change("network -> collection count changed: $count")
            class CollectionCountUpdated(val change: Int) : Change("user -> collection count updated: $change")
            class PriceUpdated(val products: List<Product>) : Change("network -> products updated: $products")
        }

        override fun toString(): String {
            return "State(sessionId=$sessionId, card=${card?.id}, count=$count, variants=${variants.size}, " +
                "evolvesFrom=${evolvesFrom.size}, evolvesTo=${evolvesTo.size}, validation=$validation, " +
                "collection=$collectionCount, products=$products)"
        }

        companion object {

            val DEFAULT by lazy {
                State(
                    null,
                    null,
                    null,
                    null,
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    Validation(false, false, emptyList()),
                    0,
                    null
                )
            }
        }
    }
}
