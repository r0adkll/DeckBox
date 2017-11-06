package com.r0adkll.deckbuilder.arch.ui.features.importer


import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler


class DeckImportRenderer(
        val actions: DeckImportUi.Actions,
        val comp: Scheduler,
        val main: Scheduler
) : DisposableStateRenderer<DeckImportUi.State>() {

    override fun start() {

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

        disposables += state
                .map { it.cards }
                .distinctUntilChanged()
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe { actions.setResults(it) }
    }
}