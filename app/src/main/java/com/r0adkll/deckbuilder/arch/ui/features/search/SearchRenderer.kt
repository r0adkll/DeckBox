package com.r0adkll.deckbuilder.arch.ui.features.search

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.renderers.DisposableStateRenderer
import com.ftinc.kit.arch.util.mapNullable
import com.ftinc.kit.arch.util.plusAssign
import io.reactivex.Scheduler

class SearchRenderer(
    val actions: SearchUi.Actions,
    main: Scheduler,
    comp: Scheduler
) : DisposableStateRenderer<SearchUi.State>(main, comp) {

    @SuppressLint("RxSubscribeOnError")
    override fun start() {

        disposables += state
            .map { it.filter.isEmpty }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe {
                actions.showFilterEmpty(it)
            }

        disposables += state
            .map { it.results }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe {
                actions.setResults(it)
            }

        disposables += state
            .map { it.isLoading }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe {
                actions.showLoading(it)
            }

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

        disposables += state
            .map {
                it.results.isEmpty() && !it.isLoading && it.query.isNotBlank()
            }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe {
                if (it) {
                    actions.showEmptyResults()
                } else {
                    actions.showEmptyDefault()
                }
            }

        disposables += state
            .map { it.query }
            .addToLifecycle()
            .subscribe {
                actions.setQueryText(it)
            }

        disposables += state
            .map { it.selected }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe { actions.setSelectedCards(it) }
    }
}
