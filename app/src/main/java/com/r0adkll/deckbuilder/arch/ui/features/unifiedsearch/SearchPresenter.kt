package com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch


import android.text.TextUtils
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Filter
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch.SearchUi.State
import com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch.SearchUi.State.Change
import com.r0adkll.deckbuilder.internal.di.scopes.FragmentScope
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject


@FragmentScope
class SearchPresenter @Inject constructor(
        val ui: SearchUi,
        val intentions: SearchUi.Intentions,
        val repository: CardRepository
) : Presenter() {

    override fun start() {

        val searchCards = intentions.searchCards()
                .flatMap { getSearchCardsObservable(it) }

        val filterChanges = intentions.filterUpdates()
                .flatMap { (category, filter) ->
                    getReSearchCardsObservable(filter)
                }

        val merged = searchCards
                .mergeWith(filterChanges)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .doOnNext { state -> Timber.v("    --- $state") }
                .subscribe(ui::render)
    }


    private fun getReSearchCardsObservable(filter: Filter): Observable<Change> {
        if (ui.state.query.isBlank() && filter.isEmpty) {
            return Observable.just(Change.FilterChanged(filter) as Change)
        }
        else {
            val query = ui.state.query
            return repository.search(filter.superType, query.replace(",", "|"), filter)
                    .map { Change.ResultsLoaded(it) as Change }
                    .startWith(listOf(
                            Change.FilterChanged(filter) as Change,
                            Change.IsLoading as Change
                    ))
                    .onErrorReturn(handleUnknownError())
        }
    }


    private fun getSearchCardsObservable(text: String): Observable<Change> {
        val filter = ui.state.filter
        return if (TextUtils.isEmpty(text) && filter.isEmpty) {
            Observable.just(Change.ClearQuery as Change)
        }
        else {
            repository.search(filter.superType, text.replace(",", "|"), filter)
                    .map { Change.ResultsLoaded(it) as Change }
                    .startWith(listOf(
                            Change.QuerySubmitted(text) as Change,
                            Change.IsLoading as Change
                    ))
                    .onErrorReturn(handleUnknownError())
        }
    }


    companion object {

        private fun handleUnknownError(): (Throwable) -> Change = { t ->
            Timber.e(t, "Error occurred during my search")
            Change.Error(t.localizedMessage ?: t.message ?: "Unknown error has occured")
        }
    }
}