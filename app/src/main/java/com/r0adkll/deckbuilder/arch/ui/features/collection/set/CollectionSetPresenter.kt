package com.r0adkll.deckbuilder.arch.ui.features.collection.set

import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.collection.repository.CollectionRepository
import com.r0adkll.deckbuilder.arch.ui.features.collection.set.CollectionSetUi.State
import com.r0adkll.deckbuilder.arch.ui.features.collection.set.CollectionSetUi.State.Change
import io.reactivex.Observable
import javax.inject.Inject

class CollectionSetPresenter @Inject constructor(
    ui: CollectionSetUi,
    val intentions: CollectionSetUi.Intentions,
    val cardRepository: CardRepository,
    val collectionRepository: CollectionRepository
) : UiPresenter<State, Change>(ui) {

    override fun smashObservables(): Observable<Change> {

        val cards = cardRepository.search(null, "", ui.state.searchFilter)
            .map { Change.Cards(it) as Change }
            .startWith(Change.IsLoading as Change)
            .onErrorReturn { Change.Error("Unable to load this expansions cards") }

        val counts = collectionRepository.getCountForSet(ui.state.expansion!!.code)
            .map { Change.Counts(it) as Change }
            .onErrorReturn { Change.Error("Unable to load your collection counts") }

        val addCard = intentions.addCard()
            .flatMap { addedCard ->
                Observable.fromIterable(addedCard)
                    .flatMap { card ->
                        collectionRepository.incrementCount(card)
                    }
                    .toList()
                    .toObservable()
                    .map { Change.CountsUpdated(emptyList()) as Change }
                    .startWith(
                        addedCard.map {
                            Change.CountChanged(it, 1)
                        }
                    )
                    .onErrorReturn { Change.Error("Unable to add card to collection") }
            }

        val removeCard = intentions.removeCard()
            .flatMap { removedCard ->
                collectionRepository.decrementCount(removedCard)
                    .map { Change.CountsUpdated(emptyList()) as Change }
                    .startWith(Change.CountChanged(removedCard, -1))
                    .onErrorReturn { Change.Error("Unable to add card to collection") }
            }

        val addSet = intentions.addSet()
            .flatMap {
                collectionRepository.incrementSet(ui.state.expansion!!.code, ui.state.cards)
                    .map { Change.CountsUpdated(it) as Change }
                    .onErrorReturn { Change.Error("Something went wrong incrementing set in collection") }
            }

        val toggleMissingCards = intentions.toggleMissingCards()
            .map { Change.ToggleMissingCards as Change }

        return cards.mergeWith(counts)
            .mergeWith(addCard)
            .mergeWith(removeCard)
            .mergeWith(addSet)
            .mergeWith(toggleMissingCards)
    }
}
