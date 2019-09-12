package com.r0adkll.deckbuilder.arch.ui.features.importer


import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.domain.features.importer.repository.Importer
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.importer.DeckImportUi.State
import com.r0adkll.deckbuilder.arch.ui.features.importer.DeckImportUi.State.Change
import com.r0adkll.deckbuilder.util.extensions.logState
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import timber.log.Timber
import javax.inject.Inject


class DeckImportPresenter @Inject constructor(
        val ui: DeckImportUi,
        val intentions: DeckImportUi.Intentions,
        val importer: Importer
) : Presenter() {

    @SuppressLint("RxSubscribeOnError")
    override fun start() {

        val conversion = intentions.importDeckList()
                .flatMap { deckList ->
                    importer.import(deckList)
                            .map { Change.DeckListConverted(it) as Change }
                            .startWith(Change.IsLoading as Change)
                            .onErrorReturn(handleUnknownError)
                }
                .doOnNext { Timber.d(it.logText) }

        disposables += conversion.scan(ui.state, State::reduce)
                .logState()
                .subscribe { ui.render(it) }

    }


    companion object {

        private val handleUnknownError: (Throwable) -> Change = { Change.Error("Error importing your decklist") }
    }
}
