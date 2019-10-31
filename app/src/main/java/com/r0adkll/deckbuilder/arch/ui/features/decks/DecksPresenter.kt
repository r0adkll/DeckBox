package com.r0adkll.deckbuilder.arch.ui.features.decks

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.domain.features.community.repository.CommunityRepository
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.ValidatedDeck
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.domain.features.preview.PreviewRepository
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import com.r0adkll.deckbuilder.arch.domain.features.validation.repository.DeckValidator
import com.r0adkll.deckbuilder.arch.ui.features.decks.DecksUi.State
import com.r0adkll.deckbuilder.arch.ui.features.decks.DecksUi.State.Change
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.internal.di.scopes.FragmentScope
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

@FragmentScope
class DecksPresenter @Inject constructor(
        ui: DecksUi,
        val intentions: DecksUi.Intentions,
        val deckRepository: DeckRepository,
        val communityRepository: CommunityRepository,
        val editRepository: EditRepository,
        val previewRepository: PreviewRepository,
        val validator: DeckValidator,
        val remote: Remote,
        val preferences: AppPreferences
) : UiPresenter<State, Change>(ui) {

    @SuppressLint("RxSubscribeOnError")
    override fun smashObservables(): Observable<Change> {
        val loadDecks = deckRepository.getDecks()
                .flatMap {
                    Observable.fromIterable(it)
                            .flatMap { deck ->
                                validator.validate(deck.cards)
                                        .map { validation ->
                                            ValidatedDeck(deck, validation)
                                        }
                            }
                            .toList()
                            .toObservable()
                }
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

        val showPreview = previewRepository.getExpansionPreview()
                .map { Change.ShowPreview(it) as Change }
                .onErrorReturnItem(Change.HidePreview as Change)

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
                .subscribe {
                    previewRepository.dismissPreview()
                }

        return loadDecks.mergeWith(deleteDecks)
                .mergeWith(createSession)
                .mergeWith(createNewSession)
                .mergeWith(clearSession)
                .mergeWith(showQuickStart)
                .mergeWith(showPreview)
    }

    companion object {

        private val handleUnknownError: (Throwable) -> Change = { t ->
            Timber.e(t, "Error getting decks")
            Change.Error("Something went wrong when trying to load your decks.")
        }
    }
}
