package com.r0adkll.deckbuilder.arch.ui.features.collection

import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.collection.repository.CollectionRepository
import com.r0adkll.deckbuilder.arch.ui.features.collection.CollectionUi.State
import com.r0adkll.deckbuilder.arch.ui.features.collection.CollectionUi.State.Change
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject


class CollectionPresenter @Inject constructor(
        ui: CollectionUi,
        val intentions: CollectionUi.Intentions,
        val collectionRepository: CollectionRepository,
        val cardRepository: CardRepository
) : UiPresenter<State, Change>(ui) {

    override fun smashObservables(): Observable<Change> {

        val expansions = cardRepository.getExpansions()
                .map { Change.Expansions(it) as Change }
                .onErrorReturn(handleUnknownError)

        val counts = collectionRepository.observeAll()
                .map { Change.Counts(it) as Change }
                .onErrorReturn(handleUnknownError)

        return expansions.mergeWith(counts)
    }

    companion object {

        val handleUnknownError: (Throwable) -> Change = {
            Timber.e(it, "Unknown error getting collection data")
            Change.Error("Unable to load your collection")
        }
    }
}