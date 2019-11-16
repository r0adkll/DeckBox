package com.r0adkll.deckbuilder.arch.ui.features.settings.cache

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.renderers.UiBaseStateRenderer
import com.ftinc.kit.arch.util.plusAssign
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.CacheStatus
import com.r0adkll.deckbuilder.arch.ui.components.ExpansionComparator
import com.r0adkll.deckbuilder.arch.ui.features.settings.cache.ManageCacheUi.*
import com.r0adkll.deckbuilder.arch.ui.features.settings.cache.adapter.ExpansionCache
import com.r0adkll.deckbuilder.util.AppSchedulers
import com.r0adkll.deckbuilder.util.extensions.displayableFileSize
import com.r0adkll.deckbuilder.util.extensions.sumByLong

class ManageCacheRenderer(
    actions: Actions,
    schedulers: AppSchedulers
) : UiBaseStateRenderer<State, State.Change, Actions>(actions, schedulers.main, schedulers.comp) {

    @SuppressLint("RxSubscribeOnError")
    override fun onStart() {

        disposables += state
            .map { s ->
                val totalSizeInBytes = s.offlineStatus.expansions.values.sumByLong {
                    when (it) {
                        is CacheStatus.Cached -> it.totalSizeInBytes
                        else -> 0
                    }
                }
                totalSizeInBytes
            }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe {
                val (size, label) = it.displayableFileSize()
                actions.setTotalSize(size, label)
            }

        disposables += state
            .map { s ->
                val cachedExpansions = s.offlineStatus.expansions.filterValues {
                    it is CacheStatus.Cached
                }.toSortedMap(ExpansionComparator())

                cachedExpansions.map { (expansion, cacheStatus) ->
                    ExpansionCache(expansion, cacheStatus as CacheStatus.Cached)
                }
            }
            .distinctUntilChanged()
            .addToLifecycle()
            .subscribe {
                actions.setItems(it)
            }

    }
}
