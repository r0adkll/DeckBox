package com.r0adkll.deckbuilder.arch.ui.features.testing

import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.r0adkll.deckbuilder.arch.domain.features.testing.DeckTester
import com.r0adkll.deckbuilder.arch.ui.features.testing.DeckTestingUi.State.*
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject


class DeckTestingPresenter @Inject constructor(
        ui: DeckTestingUi,
        val intentions: DeckTestingUi.Intentions,
        val tester: DeckTester
) : UiPresenter<DeckTestingUi.State, DeckTestingUi.State.Change>(ui) {

    override fun smashObservables(): Observable<DeckTestingUi.State.Change> {
        return intentions.runTests()
                .flatMap {
                    tester.testDeck(ui.state.sessionId, it)
                            .map { Change.Results(it) as Change }
                            .startWith(Change.IsLoading as Change)
                            .onErrorReturn(handleUnknownError)
                }
    }


    companion object {

        val handleUnknownError: (Throwable) -> Change = {
            Timber.e(it, "Unknown error testing deck")
            Change.Error("Unable to test this deck")
        }
    }
}