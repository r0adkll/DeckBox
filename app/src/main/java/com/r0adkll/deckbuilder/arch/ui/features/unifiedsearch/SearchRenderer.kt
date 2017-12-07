package com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch


import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler


class SearchRenderer(
        val actions: SearchUi.Actions,
        val main: Scheduler,
        val comp: Scheduler
) : DisposableStateRenderer<SearchUi.State>() {

    override fun start() {

        disposables += state
                .map { it.filter.isEmpty }
                .distinctUntilChanged()
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe { actions.showFilterEmpty(it) }

        disposables += state
                .map { it.results }
                .distinctUntilChanged()
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe { actions.setResults(it) }

        disposables += state
                .map { it.isLoading }
                .distinctUntilChanged()
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe { actions.showLoading(it) }

        disposables += state
                .mapNullable { it.error }
                .distinctUntilChanged()
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe {
                    val value = it.value
                    if (value != null) {
                        actions.showError(value)
                    } else {
                        actions.hideError()
                    }
                }

        // Subscribe to query text changes
        disposables += state
                .map { it.query }
                .distinctUntilChanged()
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe { actions.setQueryText(it) }
    }
}