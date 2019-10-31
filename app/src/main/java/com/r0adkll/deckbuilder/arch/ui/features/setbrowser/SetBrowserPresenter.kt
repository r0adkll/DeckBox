package com.r0adkll.deckbuilder.arch.ui.features.setbrowser

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.ui.features.setbrowser.SetBrowserUi.State
import com.r0adkll.deckbuilder.arch.ui.features.setbrowser.SetBrowserUi.State.Change
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class SetBrowserPresenter @Inject constructor(
        ui: SetBrowserUi,
        val intentions: SetBrowserUi.Intentions,
        val repository: CardRepository
) : UiPresenter<State, Change>(ui) {

    @SuppressLint("RxSubscribeOnError")
    override fun smashObservables(): Observable<Change> {

        val loadCards = repository.findByExpansion(ui.state.setCode)
                .map { Change.CardsLoaded(it) as Change }
                .startWith(Change.IsLoading as Change)
                .onErrorReturn(handleUnknownError)

        val changeFilter = intentions.filterChanged()
                .map { Change.FilterChanged(it) as Change }

        return loadCards.mergeWith(changeFilter)
    }

    companion object {

        val handleUnknownError: (Throwable) -> Change = {
            Timber.e(it, "Error loading cards")
            Change.Error("Unable to load cards for this set")
        }
    }
}
