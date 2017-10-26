package com.r0adkll.deckbuilder.arch.ui.features.search


import android.text.TextUtils
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.search.SearchUi.State
import com.r0adkll.deckbuilder.arch.ui.features.search.SearchUi.State.Change
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.pokemontcg.model.SuperType
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
                .flatMap { getSearchCardsObservable(ui.state.category, it) }

        val selectCard = intentions.selectCard()
                .map { Change.CardSelected(it) as Change }

        val switchCategories = intentions.switchCategories()
                .map { Change.CategorySwitched(it) as Change }

        val clearSelection = intentions.clearSelection()
                .map { Change.ClearSelectedCards as Change }

        val filterChanges = intentions.filterUpdates()
                .flatMap { (category, filter) ->
                    getReSearchCardsObservable(category, filter)
                }

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


    private fun getReSearchCardsObservable(category: SuperType, filter: Filter): Observable<Change> {
        val result = ui.state.results[category]
        if (result?.query.isNullOrBlank()) {
            return Observable.just(Change.FilterChanged(category, filter) as Change)
        }
        else {
            val query = result?.query ?: ""
            return repository.search(category, query.replace(",", "|"), filter)
                    .map { Change.ResultsLoaded(category, it) as Change }
                    .startWith(listOf(
                            Change.FilterChanged(category, filter) as Change,
                            Change.IsLoading(category) as Change
                    ))
                    .onErrorReturn(handleUnknownError(category))
        }
    }


    private fun getSearchCardsObservable(category: SuperType, text: String): Observable<Change> {
        return if (TextUtils.isEmpty(text)) {
            Observable.just(Change.ClearQuery(category) as Change)
        }
        else {
            repository.search(ui.state.category, text.replace(",", "|"), ui.state.current()?.filter)
                    .map { Change.ResultsLoaded(category, it) as Change }
                    .startWith(listOf(
                            Change.QuerySubmitted(category, text) as Change,
                            Change.IsLoading(category) as Change
                    ))
                    .onErrorReturn(handleUnknownError(category))
        }
    }


    companion object {

        private fun handleUnknownError(category: SuperType): (Throwable) -> Change = { t ->
            Change.Error(category, t.localizedMessage ?: t.message ?: "Unknown error has occured")
        }
    }
}