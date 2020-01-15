package com.r0adkll.deckbuilder.arch.ui.features.overview

import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.ftinc.kit.arch.util.plusAssign
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Edit
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.ui.features.overview.OverviewUi.State.Change
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

class OverviewPresenter @Inject constructor(
    ui: OverviewUi,
    val intentions: OverviewUi.Intentions,
    val editor: EditRepository
) : UiPresenter<OverviewUi.State, Change>(ui) {

    override fun smashObservables(): Observable<Change> {

        disposables += intentions.addCards()
            .flatMap { editor.submit(ui.state.deckId, Edit.AddCards(it)) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.i("Added cards to Deck(${ui.state.deckId})")
            }, {
                Timber.e(it, "Error adding card to search session")
            })

        disposables += intentions.removeCard()
            .flatMap { editor.submit(ui.state.deckId, Edit.RemoveCard(it)) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.i("Removed card to Deck(${ui.state.deckId})")
            }, {
                Timber.e(it, "Error removing card from search session")
            })

        return editor.observeSession(ui.state.deckId)
            .map { Change.CardsLoaded(it.cards) as Change }
            .onErrorReturn(handleUnknownError)
    }

    companion object {

        val handleUnknownError: (Throwable) -> Change = {
            Timber.e(it, "Unknown error expanding deck")
            Change.Error(it.localizedMessage ?: "Error expanding this deck")
        }
    }
}
