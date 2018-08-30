package com.r0adkll.deckbuilder.arch.ui.features.overview


import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.ui.features.overview.OverviewUi.State.*
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class OverviewPresenter @Inject constructor(
        ui: OverviewUi,
        val intentions: OverviewUi.Intentions,
        val repository: EditRepository
) : UiPresenter<OverviewUi.State, Change>(ui) {


    override fun smashObservables(): Observable<Change> {

        val observeDeck = repository.observeSession(ui.state.sessionId)
                .delay(300L, TimeUnit.MILLISECONDS)
                .map { it.cards }
                .map { Change.CardsLoaded(it) as Change }
                .onErrorReturn(handleUnknownError)

        disposables += intentions.addCards()
                .flatMap { repository.addCards(ui.state.sessionId, it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Card(s) added to session")
                }, { t -> Timber.e(t, "Error adding card to session")})

        disposables += intentions.removeCard()
                .flatMap { repository.removeCard(ui.state.sessionId, it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Card removed from session")
                }, { t -> Timber.e(t, "Error removing card from session")})

        return observeDeck
    }


    companion object {

        val handleUnknownError: (Throwable) -> OverviewUi.State.Change = {
            Timber.e(it, "Unknown error expanding deck")
            Change.Error(it.localizedMessage ?: "Error expanding this deck")
        }
    }
}