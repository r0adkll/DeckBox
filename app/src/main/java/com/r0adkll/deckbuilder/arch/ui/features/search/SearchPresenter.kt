package com.r0adkll.deckbuilder.arch.ui.features.search

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.ftinc.kit.arch.util.plusAssign
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DeckRepository
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Edit
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
    val deckRepository: DeckRepository,
    val editor: EditRepository
) : UiPresenter<State, Change>(ui) {

    override fun smashObservables(): Observable<Change> {
        val deckId = ui.state.deckId

        disposables += intentions.selectCard()
            .sessionMap(deckId) { editor.submit(deckId!!, Edit.AddCards(listOf(it))) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.i("Card added to Deck($deckId)")
            }, {
                Timber.e(it, "Error adding card to search session")
            })

        disposables += intentions.removeCard()
            .sessionMap(deckId) { editor.submit(deckId!!, Edit.RemoveCard(it)) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.i("Card removed from Deck($deckId)")
            }, {
                Timber.e(it, "Error removing card from search session")
            })

        val observeSession = if (deckId != null) {
            editor.observeSession(deckId)
                .map { Change.DeckUpdated(it) as Change }
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
    private fun <T, R> Observable<T>.sessionMap(deckId: String?, mapper: (T) -> ObservableSource<R>): Observable<R> {
        return if (deckId != null) {
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
