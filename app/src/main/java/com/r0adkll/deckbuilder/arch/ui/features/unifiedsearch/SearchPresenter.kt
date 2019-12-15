package com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch

import android.annotation.SuppressLint
import android.text.TextUtils
import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch.SearchUi.State
import com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch.SearchUi.State.Change
import com.r0adkll.deckbuilder.internal.di.scopes.FragmentScope
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

@FragmentScope
class SearchPresenter @Inject constructor(
    ui: SearchUi,
    val intentions: SearchUi.Intentions,
    val repository: CardRepository
) : UiPresenter<State, Change>(ui) {

    override fun smashObservables(): Observable<Change> {

        val searchCards = intentions.searchCards()
            .flatMap { getSearchCardsObservable(it) }

        val filterChanges = intentions.filterUpdates()
            .flatMap { filter ->
                getReSearchCardsObservable(filter)
            }

        return searchCards
            .mergeWith(filterChanges)
    }

    private fun getReSearchCardsObservable(filter: Filter): Observable<Change> {
        if (ui.state.query.isBlank() && filter.isEmptyWithoutField) {
            return Observable.just(Change.FilterChanged(filter) as Change)
        } else {
            val query = ui.state.query
            return repository.search(filter.superType, query.replace(",", "|"), filter)
                .map { Change.ResultsLoaded(it) as Change }
                .startWith(listOf(
                    Change.FilterChanged(filter) as Change,
                    Change.IsLoading as Change
                ))
                .onErrorReturn(handleUnknownError)
        }
    }

    @SuppressLint("CheckResult")
    private fun getSearchCardsObservable(text: String): Observable<Change> {
        val filter = ui.state.filter
        return if (TextUtils.isEmpty(text) && filter.isEmptyWithoutField) {
            Observable.just(Change.ClearQuery as Change)
        } else {
            repository.search(filter.superType, text.replace(",", "|"), filter)
                .map { Change.ResultsLoaded(it) as Change }
                .startWith(listOf(
                    Change.QuerySubmitted(text) as Change,
                    Change.IsLoading as Change
                ))
                .onErrorReturn(handleUnknownError)
        }
    }

    companion object {

        private val handleUnknownError: (Throwable) -> Change = { t ->
            Timber.e(t, "Error occurred during my search")
            t.printStackTrace()
            Change.Error("Unknown error has occurred")
        }
    }
}
