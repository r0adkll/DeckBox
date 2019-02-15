package com.r0adkll.deckbuilder.arch.ui.features.collection.set

import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.ftinc.kit.arch.util.plusAssign
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.collection.repository.CollectionRepository
import com.r0adkll.deckbuilder.arch.ui.features.collection.set.CollectionSetUi.State
import com.r0adkll.deckbuilder.arch.ui.features.collection.set.CollectionSetUi.State.Change
import io.reactivex.Observable
import timber.log.Timber
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

        disposables += intentions.addCard()
                .flatMap {
                    Observable.fromIterable(it)
                            .flatMap { card ->
                                collectionRepository.incrementCount(card)
                            }
                            .toList()
                            .toObservable()
                }
                .subscribe({
                    Timber.d("Cards added to collection")
                }, {
                    Timber.e(it, "Unable to add cards to collection")
                })

        disposables += intentions.removeCard()
                .flatMap {
                    collectionRepository.decrementCount(it)
                }
                .subscribe({
                    Timber.d("Cards added to collection")
                }, {
                    Timber.e(it, "Unable to add cards to collection")
                })

        return cards.mergeWith(counts)
    }
}