package com.r0adkll.deckbuilder.arch.ui.features.unifiedsearch

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.renderers.UiBaseStateRenderer
import com.ftinc.kit.arch.util.plusAssign
import io.reactivex.Scheduler

class SearchRenderer(
    actions: SearchUi.Actions,
    main: Scheduler,
    comp: Scheduler
) : UiBaseStateRenderer<SearchUi.State, SearchUi.State.Change, SearchUi.Actions>(actions, main, comp) {

    @SuppressLint("RxSubscribeOnError")
    override fun onStart() {

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
            .map { it.results.isEmpty() && !it.isLoading && it.query.isNotBlank() }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe {
                if (it) {
                    actions.showEmptyResults()
                } else {
                    actions.showEmptyDefault()
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
