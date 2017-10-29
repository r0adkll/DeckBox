package com.r0adkll.deckbuilder.arch.ui.features.decks

import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.decks.DecksUi.State
import com.r0adkll.deckbuilder.arch.ui.features.decks.DecksUi.State.*
import com.r0adkll.deckbuilder.internal.di.FragmentScope
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import timber.log.Timber
import javax.inject.Inject


@FragmentScope
class DecksPresenter @Inject constructor(
        val ui: DecksUi,
        val intentions: DecksUi.Intentions,
        val repository: DeckRepository
) : Presenter() {

    override fun start() {

        val loadDecks = repository.getDecks()
                .map { Change.DecksLoaded(it) as Change }
                .startWith(Change.IsLoading as Change)
                .onErrorReturn(handleUnknownError)

        val deleteDecks = intentions.deleteClicks()
                .flatMap {
                    repository.deleteDeck(it)
                            .map { Change.DeckDeleted as Change }
                            .onErrorReturn(handleUnknownError)
                }

        val merged = loadDecks.mergeWith(deleteDecks)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .doOnNext { state -> Timber.v("    --- $state") }
                .subscribe(ui::render)
    }


    companion object {

        private val handleUnknownError: (Throwable) -> Change = { t -> Change.Error(t.localizedMessage) }
    }
}