package com.r0adkll.deckbuilder.arch.ui.features.decks


import android.annotation.SuppressLint
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

    @SuppressLint("RxSubscribeOnError")
    override fun start() {

        disposables += state
                .map { s ->
                    val items = ArrayList<Item>()

                    if (s.preview != null) {
                        items += Item.Preview(s.preview)
                    }

                    if (s.quickStart != null) {
                        items += Item.QuickStart(s.quickStart)
                    }

                    items += s.decks
                            .sortedByDescending { it.timestamp }
                            .map { Item.DeckItem(it, s.isSessionLoading == it.id) }

                    items
                }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.showItems(it) }

        disposables += state
                .filter { it.hasLoadedOnce }
                .map { it.decks }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe { actions.balanceShortcuts(it) }

        disposables += state
                .mapNullable { it.sessionId }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    if (it.value != null) {
                        actions.openSession(it.value)
                    }
                }

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