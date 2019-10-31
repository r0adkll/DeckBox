package com.r0adkll.deckbuilder.arch.ui.features.filter

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.renderers.DisposableStateRenderer
import com.ftinc.kit.arch.util.plusAssign
import io.reactivex.Scheduler

class FilterRenderer(
        val actions: FilterUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : DisposableStateRenderer<FilterUi.State>(main, comp) {

    @SuppressLint("RxSubscribeOnError")
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
