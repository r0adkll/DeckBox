package com.r0adkll.deckbuilder.arch.ui.features.search.filter


import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler


class FilterRenderer(
        val actions: FilterUi.Actions,
        val main: Scheduler,
        val comp: Scheduler
) : DisposableStateRenderer<FilterUi.State>() {

    override fun start() {

        disposables += state
                .map { it.filters[it.category]!!.applySpecification() }
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe { actions.setItems(it) }

        disposables += state
                .map { it.filters[it.category]!!.filter.isEmpty }
                .distinctUntilChanged()
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe { actions.setIsEmpty(it) }

    }
}