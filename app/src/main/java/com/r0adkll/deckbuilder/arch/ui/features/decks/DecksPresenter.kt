package com.r0adkll.deckbuilder.arch.ui.features.decks

import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.decks.DecksUi.State
import com.r0adkll.deckbuilder.arch.ui.features.decks.DecksUi.State.*
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.internal.di.scopes.FragmentScope
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import timber.log.Timber
import javax.inject.Inject


@FragmentScope
class DecksPresenter @Inject constructor(
        val ui: DecksUi,
        val intentions: DecksUi.Intentions,
        val repository: DeckRepository
) : Presenter() {

    @SuppressLint("CheckResult")
    override fun start() {

        val loadDecks = repository.getDecks()
                .map { Change.DecksLoaded(it) as Change }
//                .startWith(Change.IsLoading as Change)
                .onErrorReturn(handleUnknownError)

        val deleteDecks = intentions.deleteClicks()
                .flatMap {
                    Analytics.event(Event.SelectContent.Action("delete_deck"))
                    repository.deleteDeck(it)
                            .map { Change.DeckDeleted as Change }
                            .onErrorReturn(handleUnknownError)
                }

        val merged = loadDecks.mergeWith(deleteDecks)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .doOnNext { state -> Timber.v("    --- $state") }
                .subscribe(ui::render)

        disposables += intentions.duplicateClicks()
                .flatMap {
                    Analytics.event(Event.SelectContent.Action("duplicate_deck"))
                    repository.duplicateDeck(it)
                            .onErrorReturn { handleUnknownError }
                }
                .subscribe {
                    Timber.i("Deck duplicated!")
                }
    }


    companion object {

        private val handleUnknownError: (Throwable) -> Change = { t ->
            Timber.e(t, "Error getting decks")
            Change.Error(t.localizedMessage)
        }
    }
}