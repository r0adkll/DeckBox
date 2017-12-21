package com.r0adkll.deckbuilder.arch.ui.features.filter


import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler


class FilterRenderer(
        val actions: FilterUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : DisposableStateRenderer<FilterUi.State>(main, comp) {

    override fun start() {

        disposables += state
                .map { it.filters[it.category]!!.applySpecification() }
                .addToLifecycle()
                .subscribe { actions.setItems(it) }

        disposables += state
                .map { it.filters[it.category]!!.filter.isEmpty }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setIsEmpty(it) }

    }
}