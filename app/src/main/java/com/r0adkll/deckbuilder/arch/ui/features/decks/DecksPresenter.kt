package com.r0adkll.deckbuilder.arch.ui.features.decks

import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.remote.Remote
import com.r0adkll.deckbuilder.arch.domain.features.community.repository.CommunityRepository
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.decks.DecksUi.State
import com.r0adkll.deckbuilder.arch.ui.features.decks.DecksUi.State.*
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.internal.di.scopes.FragmentScope
import com.r0adkll.deckbuilder.util.extensions.iso8601
import com.r0adkll.deckbuilder.util.extensions.logState
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject


@FragmentScope
class DecksPresenter @Inject constructor(
        val ui: DecksUi,
        val intentions: DecksUi.Intentions,
        val deckRepository: DeckRepository,
        val communityRepository: CommunityRepository,
        val editRepository: EditRepository,
        val remote: Remote,
        val preferences: AppPreferences
) : Presenter() {

    @SuppressLint("CheckResult")
    override fun start() {

        val loadDecks = deckRepository.getDecks()
                .map { Change.DecksLoaded(it) as Change }
//                .startWith(Change.IsLoading as Change)
                .onErrorReturn(handleUnknownError)

        val deleteDecks = intentions.deleteClicks()
                .flatMap { deck ->
                    Analytics.event(Event.SelectContent.Action("delete_deck"))
                    deckRepository.deleteDeck(deck)
                            .map { Change.DeckDeleted as Change }
                            .onErrorReturn(handleUnknownError)
                }

        val createSession = intentions.createSession()
                .flatMap { deck ->
                    editRepository.createSession(deck, null)
                            .map { Change.SessionLoaded(it) as Change }
                            .startWith(Change.IsSessionLoading(deck.id))
                            .onErrorReturn(handleUnknownError)
                }

        val createNewSession = intentions.createNewSession()
                .flatMap {
                    editRepository.createSession()
                            .map { Change.SessionLoaded(it) as Change }
                            .startWith(Change.IsSessionLoading(""))
                            .onErrorReturn(handleUnknownError)
                }

        val clearSession = intentions.clearSession()
                .map { Change.ClearSession as Change }

        val showPreview = preferences.previewVersion
                .asObservable()
                .map { version ->
                    val preview = remote.expansionPreview
                    if (preview != null && // If preview exists
                            preview.version > version && // If we haven't dismissed this version
                            preview.expiresAt.iso8601() > System.currentTimeMillis()) { // If the preview hasn't expired
                        Change.ShowPreview(preview)
                    } else {
                        Change.HidePreview
                    }
                }

        val showQuickStart = preferences.quickStart
                .asObservable()
                .flatMap { canQuickStart ->
                    if (canQuickStart) {
                        communityRepository.getDeckTemplates()
                                .map { DecksUi.QuickStart(it) }
                                .map { Change.ShowQuickStart(it) }
                                .startWith(Change.ShowQuickStart(DecksUi.QuickStart()))
                    } else {
                        Observable.just(Change.HideQuickStart)
                    }
                }

        val merged = loadDecks.mergeWith(deleteDecks)
                .mergeWith(createSession)
                .mergeWith(createNewSession)
                .mergeWith(clearSession)
                .mergeWith(showQuickStart)
                .mergeWith(showPreview)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .logState()
                .subscribe(ui::render)

        disposables += intentions.duplicateClicks()
                .flatMap {
                    Analytics.event(Event.SelectContent.Action("duplicate_deck"))
                    deckRepository.duplicateDeck(it)
                }
                .subscribe({
                    Timber.i("Deck duplicated!")
                }, {
                    Timber.e(it, "Error duplicating deck")
                })

        disposables += intentions.dismissPreview()
                .subscribe { _ ->
                    remote.expansionPreview?.let {
                        preferences.previewVersion.set(it.version)
                    }
                }
    }


    companion object {

        private val handleUnknownError: (Throwable) -> Change = { t ->
            Timber.e(t, "Error getting decks")
            Change.Error("Something went wrong when trying to load your decks.")
        }
    }
}