package com.r0adkll.deckbuilder.arch.ui.features.importer

import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler

class DeckImportRenderer(
        val actions: DeckImportUi.Actions,
        comp: Scheduler,
        main: Scheduler
) : DisposableStateRenderer<DeckImportUi.State>(main, comp) {

    @SuppressLint("RxSubscribeOnError")
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
                    val value = it.value
                    if (value != null) {
                        actions.showError(value)
                    } else {
                        actions.hideError()
                    }
                }

        disposables += state
                .map { it.cards }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.setResults(it) }
    }
}
