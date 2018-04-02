package com.r0adkll.deckbuilder.arch.ui.features.testing


import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler


class DeckTestingRenderer(
        val actions: DeckTestingUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : DisposableStateRenderer<DeckTestingUi.State>(main, comp) {

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
                    if (it.value == null) {
                        actions.hideError()
                    } else {
                        actions.showError(it.value)
                    }
                }
    }
}