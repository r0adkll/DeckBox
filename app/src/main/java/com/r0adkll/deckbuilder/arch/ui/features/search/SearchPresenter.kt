package com.r0adkll.deckbuilder.arch.ui.features.search

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.ftinc.kit.arch.util.plusAssign
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Session
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.ui.features.search.SearchUi.State
import com.r0adkll.deckbuilder.arch.ui.features.search.SearchUi.State.Change
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

class SearchPresenter @Inject constructor(
    ui: SearchUi,
    val intentions: SearchUi.Intentions,
    val repository: CardRepository,
    val editor: EditRepository
) : UiPresenter<State, Change>(ui) {

    override fun smashObservables(): Observable<Change> {

        disposables += intentions.selectCard()
            .sessionMap(ui.state.sessionId) { editor.addCards(ui.state.sessionId, listOf(it), ui.state.id) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Card added to search session")
            }, { Timber.e(it, "Error adding card to search session") })

        disposables += intentions.removeCard()
            .sessionMap(ui.state.sessionId) { editor.removeCard(ui.state.sessionId, it, ui.state.id) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Card removed from search session")
            }, { Timber.e(it, "Error removing card from search session") })

        disposables += intentions.clearSelection()
            .sessionMap(ui.state.sessionId) { editor.clearSearchSession(ui.state.sessionId, ui.state.id) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("Search session cleared.")
            }, { Timber.e(it, "Error clearing search session") })

        val observeSession = if (ui.state.sessionId != Session.NO_ID) {
            editor.observeSession(ui.state.sessionId)
                .map { Change.SessionUpdated(it) as Change }
                .onErrorReturn(handleUnknownError())
        } else {
            Observable.empty()
        }

        val searchCards = intentions.searchCards()
            .flatMap { getSearchCardsObservable(it) }

        val filterChanges = intentions.filterUpdates()
            .flatMap { filter ->
                getReSearchCardsObservable(filter)
            }

        return observeSession
            .mergeWith(searchCards)
            .mergeWith(filterChanges)
    }

    /**
     * Helper extensions function for determining is we have a valid sessionId, and if not to just return
     * an empty observable. This enables us to use the search activity without having a session to
     * be modified
     */
    private fun <T, R> Observable<T>.sessionMap(sessionId: Long, mapper: (T) -> ObservableSource<R>): Observable<R> {
        return if (sessionId != Session.NO_ID) {
            this.flatMap(mapper)
        } else {
            Observable.empty()
        }
    }

    private fun getReSearchCardsObservable(filter: Filter): Observable<Change> {
        return if (ui.state.query.isBlank() && filter.isEmptyWithoutField) {
            Observable.just(Change.FilterChanged(filter) as Change)
        } else {
            val query = ui.state.query
            repository.search(null, query.replace(",", "|"), filter)
                .map { Change.ResultsLoaded(it) as Change }
                .startWith(listOf(
                    Change.FilterChanged(filter) as Change,
                    Change.IsLoading as Change
                ))
                .onErrorReturn(handleUnknownError())
        }
    }

    @SuppressLint("CheckResult")
    private fun getSearchCardsObservable(text: String): Observable<Change> {
        val filter = ui.state.filter
        return if (text.isEmpty() && filter.isEmptyWithoutField) {
            Observable.just(Change.ClearQuery as Change)
        } else {
            repository.search(null, text.replace(",", "|"), filter)
                .map { Change.ResultsLoaded(it) as Change }
                .startWith(listOf(
                    Change.QuerySubmitted(text) as Change,
                    Change.IsLoading as Change
                ))
                .onErrorReturn(handleUnknownError())
        }
    }

    companion object {

        private fun handleUnknownError(): (Throwable) -> Change = {
            Change.Error("Unknown error has occured")
        }
    }
}
