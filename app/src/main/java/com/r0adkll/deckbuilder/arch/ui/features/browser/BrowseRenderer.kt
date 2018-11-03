package com.r0adkll.deckbuilder.arch.ui.features.browser


import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.ui.components.renderers.DisposableStateRenderer
import com.r0adkll.deckbuilder.arch.ui.features.browser.adapter.Item
import com.r0adkll.deckbuilder.util.extensions.mapNullable
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import io.reactivex.Scheduler


class BrowseRenderer(
        val actions: BrowseUi.Actions,
        main: Scheduler,
        comp: Scheduler
) : DisposableStateRenderer<BrowseUi.State>(main, comp) {

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
                    if (it.isNonNull()) {
                        actions.showError(it.value!!)
                    } else {
                        actions.hideError()
                    }
                }

        disposables += state
                .map { s ->
                    val items = s.expansions.map {
                        val cacheStatus = s.offlineStatus?.expansions?.get(it.code)
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