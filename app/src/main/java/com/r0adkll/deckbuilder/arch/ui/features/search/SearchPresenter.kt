package com.r0adkll.deckbuilder.arch.ui.features.search


import android.text.TextUtils
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.search.SearchUi.State
import com.r0adkll.deckbuilder.arch.ui.features.search.SearchUi.State.Change
import com.r0adkll.deckbuilder.arch.ui.features.search.filter.di.CategoryIntentions
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject


class SearchPresenter @Inject constructor(
        val ui: SearchUi,
        val intentions: SearchUi.Intentions,
        val repository: CardRepository
) : Presenter() {

    override fun start() {

        val searchCards = intentions.searchCards()
                .flatMap { getSearchCardsObservable(it) }

        val selectCard = intentions.selectCard()
                .map { Change.CardSelected(it) as Change }

        val switchCategories = intentions.switchCategories()
                .map { Change.CategorySwitched(it) as Change }

        val clearSelection = intentions.clearSelection()
                .map { Change.ClearSelectedCards as Change }

        val filterChanges = intentions.filterUpdates()
                .flatMap { getSearchCardsObservable(ui.state.current()?.query ?: "", it) }

        val merged = searchCards
                .mergeWith(selectCard)
                .mergeWith(clearSelection)
                .mergeWith(switchCategories)
                .mergeWith(filterChanges)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .doOnNext { state -> Timber.v("    --- $state") }
                .subscribe(ui::render)
    }


    private fun getSearchCardsObservable(text: String, filter: Filter? = null): Observable<Change> {
        return if (TextUtils.isEmpty(text)) {
            if (filter == null) {
                Observable.fromArray(Change.ClearQuery as Change)
            }
            else {
                Observable.fromArray(
                        Change.FilterChanged(filter) as Change,
                        Change.ClearQuery as Change
                )
            }
        }
        else {
            val ftr = filter ?: ui.state.current()?.filter
            val start = mutableListOf(
                    Change.QuerySubmitted(text) as Change,
                    Change.IsLoading as Change
            )

            if (filter != null) {
                start.add(0, Change.FilterChanged(filter))
            }

            repository.search(ui.state.category, text.replace(",", "|"), ftr)
                    .map { Change.ResultsLoaded(it) as Change }
                    .startWith(start)
                    .onErrorReturn(handleUnknownError)
        }
    }


    companion object {

        private val handleUnknownError: (Throwable) -> Change = { t -> Change.Error(t.localizedMessage ?: t.message ?: "Unknown error has occured") }
    }
}