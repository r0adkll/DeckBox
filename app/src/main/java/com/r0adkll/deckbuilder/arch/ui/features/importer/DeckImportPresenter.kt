package com.r0adkll.deckbuilder.arch.ui.features.importer

import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.r0adkll.deckbuilder.arch.domain.features.importer.repository.Importer
import com.r0adkll.deckbuilder.arch.ui.features.importer.DeckImportUi.State
import com.r0adkll.deckbuilder.arch.ui.features.importer.DeckImportUi.State.Change
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class DeckImportPresenter @Inject constructor(
        ui: DeckImportUi,
        val intentions: DeckImportUi.Intentions,
        val importer: Importer
) : UiPresenter<State, Change>(ui) {

    override fun smashObservables(): Observable<Change> {
        return intentions.importDeckList()
                .flatMap { deckList ->
                    importer.import(deckList)
                            .map { Change.DeckListConverted(it) as Change }
                            .startWith(Change.IsLoading as Change)
                            .onErrorReturn(handleUnknownError)
                }
                .doOnNext { Timber.d(it.logText) }
    }

    companion object {

        private val handleUnknownError: (Throwable) -> Change = { Change.Error("Error importing your decklist") }
    }
}
