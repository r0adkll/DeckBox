package com.r0adkll.deckbuilder.arch.ui.features.browser


import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler


class BrowseRenderer(
        val actions: BrowseUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : DisposableStateRenderer<BrowseUi.State>(main, comp) {

    override fun start() {

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
                    if (it.isNonNull()) {
                        actions.showError(it.value!!)
                    } else {
                        actions.hideError()
                    }
                }

        disposables += state
                .map { it.expansions }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setExpansions(it) }
    }
}