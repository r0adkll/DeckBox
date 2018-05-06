package com.r0adkll.deckbuilder.arch.ui.features.decks

import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.decks.DecksUi.State
import com.r0adkll.deckbuilder.arch.ui.features.decks.DecksUi.State.*
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.internal.di.scopes.FragmentScope
import com.r0adkll.deckbuilder.util.extensions.logState
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import timber.log.Timber
import javax.inject.Inject


@FragmentScope
class DecksPresenter @Inject constructor(
        val ui: DecksUi,
        val intentions: DecksUi.Intentions,
        val repository: DeckRepository,
        val preferences: AppPreferences
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

        val showPreview = preferences.previewNewExpansion
                .asObservable()
                .map { Change.ShowPreview(it) as Change }

        val merged = loadDecks.mergeWith(deleteDecks)
                .mergeWith(showPreview)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .logState()
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

        disposables += intentions.dismissPreview()
                .subscribe {
                    preferences.previewNewExpansion.set(false)
                }
    }


    companion object {

        private val handleUnknownError: (Throwable) -> Change = { t ->
            Timber.e(t, "Error getting decks")
            Change.Error("Something went wrong when trying to load your decks.")
        }
    }
}