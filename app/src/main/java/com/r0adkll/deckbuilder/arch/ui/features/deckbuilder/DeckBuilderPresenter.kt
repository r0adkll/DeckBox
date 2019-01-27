package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder


import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.domain.features.validation.repository.DeckValidator
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderUi.State
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderUi.State.*
import com.r0adkll.deckbuilder.util.extensions.logState
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject


@SuppressLint("CheckResult")
class DeckBuilderPresenter @Inject constructor(
        val ui: DeckBuilderUi,
        val intentions: DeckBuilderUi.Intentions,
        val repository: EditRepository,
        val validator: DeckValidator
) : Presenter() {

    @SuppressLint("RxSubscribeOnError")
    override fun start() {

        val observeSession = repository.observeSession(ui.state.sessionId)
                .flatMap { session ->
                    validator.validate(session.cards)
                            .map { Change.Validated(it) as Change }
                            .startWith(Change.SessionUpdated(session) as Change)
                            .onErrorReturn(handleUnknownError)
                }

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

        disposables += intentions.editDeckName()
                .flatMap { repository.changeName(ui.state.sessionId, it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Name changed!")
                }, { t -> Timber.e(t, "Error changing deck name")})

        disposables += intentions.editDeckDescription()
                .flatMap { repository.changeDescription(ui.state.sessionId, it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Description changed!")
                }, { t -> Timber.e(t, "Error changing description name")})

        val editDeck = intentions.editDeckClicks()
                .map { Change.Editing(it) as Change }

        val editOverview = intentions.editOverviewClicks()
                .map { Change.Overview(it) as Change }

        val saveDeck = intentions.saveDeck()
                .flatMap {
                    repository.persistSession(ui.state.sessionId)
                            .map { Change.Saved as Change }
                            .startWith(Change.Saving as Change)
                            .onErrorReturn(handlePersistError)
                }

        val merged = observeSession
                .mergeWith(editDeck)
                .mergeWith(editOverview)
                .mergeWith(saveDeck)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .logState()
                .subscribe(ui::render)
    }


    companion object {

        private val handleUnknownError: (Throwable) -> Change = { t ->
            Timber.e(t, "Error processing deck")
            Change.Error("Error validating your deck")
        }

        private val handlePersistError: (Throwable) -> Change = { t ->
            Timber.e(t, "Error saving deck")
            Change.Error("Error saving your deck")
        }
    }
}