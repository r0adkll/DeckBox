package com.r0adkll.deckbuilder.arch.ui.features.browser

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.renderers.UiBaseStateRenderer
import com.ftinc.kit.arch.util.plusAssign
import com.r0adkll.deckbuilder.arch.ui.features.browser.adapter.Item

import io.reactivex.Scheduler

class BrowseRenderer(
    actions: BrowseUi.Actions,
    main: Scheduler,
    comp: Scheduler
) : UiBaseStateRenderer<BrowseUi.State, BrowseUi.State.Change, BrowseUi.Actions>(actions, main, comp) {

    @SuppressLint("RxSubscribeOnError")
    override fun onStart() {

        disposables += state
            .map { s ->
                val items = s.expansions.map {
                    val cacheStatus = s.offlineStatus?.expansions?.get(it)
                    Item.ExpansionSet(it, cacheStatus) as Item
                }.toMutableList()
                if (s.offlineOutline) {
                    items.add(0, Item.OfflineOutline)
                }
                items
            }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe { actions.setExpansionsItems(it) }
    }
}
