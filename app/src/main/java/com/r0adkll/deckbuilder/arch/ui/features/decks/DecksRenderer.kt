package com.r0adkll.deckbuilder.arch.ui.features.decks


import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler


class DecksRenderer(
        val actions: DecksUi.Actions,
        val main: Scheduler,
        val comp: Scheduler
) : DisposableStateRenderer<DecksUi.State>() {

    override fun start() {

        disposables += state
                .map { it.decks }
                .distinctUntilChanged()
                .subscribeOn(comp)
                .observeOn(main)
                .subscribe { actions.showDecks(it) }

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
                    val error = it.value
                    if (error != null) {
                        actions.showError(error)
                    }
                    else {
                        actions.hideError()
                    }
                }
    }
}