package com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch


import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler


class SearchRenderer(
        val actions: SearchUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : DisposableStateRenderer<SearchUi.State>(main, comp) {

    override fun start() {

        disposables += state
                .map { it.filter.isEmpty }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.showFilterEmpty(it) }

        disposables += state
                .map { it.results }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setResults(it) }

        disposables += state
                .map { it.isLoading }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.showLoading(it) }

        disposables += state
                .mapNullable { it.error }
                .distinctUntilChanged()
                .addToLifecycle()
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
                .addToLifecycle()
                .subscribe { actions.setQueryText(it) }
    }
}