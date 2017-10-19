package com.r0adkll.deckbuilder.arch.ui.features.search


import android.text.TextUtils
import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.search.SearchUi.State
import com.r0adkll.deckbuilder.arch.ui.features.search.SearchUi.State.Change
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

        val merged = searchCards
                .mergeWith(selectCard)
                .mergeWith(clearSelection)
                .mergeWith(switchCategories)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .doOnNext { state -> Timber.v("    --- $state") }
                .subscribe(ui::render)
    }


    fun getSearchCardsObservable(text: String): Observable<Change> {
        return if (TextUtils.isEmpty(text)) {
            Observable.just(Change.ClearQuery as Change)
        }
        else {
            repository.search(ui.state.category, text.replace(",", "|"))
                    .map { Change.ResultsLoaded(it) as Change }
                    .startWith(listOf(
                            Change.QuerySubmitted(text) as Change,
                            Change.IsLoading as Change
                    ))
                    .onErrorReturn(handleUnknownError)
        }
    }


    companion object {

        private val handleUnknownError: (Throwable) -> Change = { t -> Change.Error(t.localizedMessage ?: t.message ?: "Unknown error has occured") }
    }
}