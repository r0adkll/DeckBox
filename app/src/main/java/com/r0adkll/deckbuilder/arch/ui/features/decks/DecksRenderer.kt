package com.r0adkll.deckbuilder.arch.ui.features.decks


import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.Item
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler


class DecksRenderer(
        val actions: DecksUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : DisposableStateRenderer<DecksUi.State>(main, comp) {

    override fun start() {

        disposables += state
                .map {
                    val items = ArrayList<Item>()

                    if (it.showPreview) {
                        items += Item.Preview
                    }

                    items += it.decks
                            .sortedByDescending { it.timestamp }
                            .map { Item.DeckItem(it) }

                    items
                }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.showItems(it) }

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