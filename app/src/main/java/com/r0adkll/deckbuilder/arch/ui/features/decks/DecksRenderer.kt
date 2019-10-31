package com.r0adkll.deckbuilder.arch.ui.features.decks

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.renderers.UiBaseStateRenderer
import com.r0adkll.deckbuilder.arch.domain.Format
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.Item
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler

class DecksRenderer(
        actions: DecksUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : UiBaseStateRenderer<DecksUi.State, DecksUi.State.Change, DecksUi.Actions>(actions, main, comp) {

    @SuppressLint("RxSubscribeOnError")
    override fun onStart() {

        disposables += state
                .map { s ->
                    val items = ArrayList<Item>()

                    if (s.preview != null) {
                        items += Item.Preview(s.preview)
                    }

                    if (s.quickStart != null) {
                        items += Item.QuickStart(s.quickStart)
                    }

                    s.decks
                            .sortedByDescending { it.deck.timestamp }
                            .groupBy {
                                when {
                                    it.validation?.standard == true -> Format.STANDARD
                                    it.validation?.expanded == true -> Format.EXPANDED
                                    else -> Format.UNLIMITED
                                }
                            }
                            .toSortedMap()
                            .forEach { (key, value) ->
                                items += Item.Header(key.name.toLowerCase().capitalize())
                                items += value.map {
                                    Item.DeckItem(it, s.isSessionLoading == it.deck.id)
                                }
                            }

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
                .subscribe { actions.balanceShortcuts(it.map { it.deck }) }

        disposables += state
                .mapNullable { it.sessionId }
                .distinctUntilChanged()
                .addToLifecycle()
                .subscribe {
                    if (it.value != null) {
                        actions.openSession(it.value)
                    }
                }
    }
}
