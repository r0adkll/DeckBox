package com.r0adkll.deckbuilder.arch.ui.features.setbrowser

import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.setbrowser.SetBrowserUi.State
import com.r0adkll.deckbuilder.arch.ui.features.setbrowser.SetBrowserUi.State.Change
import com.r0adkll.deckbuilder.util.extensions.logState
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import timber.log.Timber
import javax.inject.Inject

class SetBrowserPresenter @Inject constructor(
        val ui: SetBrowserUi,
        val intentions: SetBrowserUi.Intentions,
        val repository: CardRepository
) : Presenter() {

    @SuppressLint("RxSubscribeOnError")
    override fun start() {

        val loadCards = repository.findByExpansion(ui.state.setCode)
                .map { Change.CardsLoaded(it) as Change }
                .startWith(Change.IsLoading as Change)
                .onErrorReturn(handleUnknownError)

        val changeFilter = intentions.filterChanged()
                .map { Change.FilterChanged(it) as Change }

        val merged = loadCards.mergeWith(changeFilter)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .logState()
                .subscribe { ui.render(it) }
    }

    companion object {

        val handleUnknownError: (Throwable) -> Change = {
            Timber.e(it, "Error loading cards")
            Change.Error("Unable to load cards for this set")
        }
    }
}
