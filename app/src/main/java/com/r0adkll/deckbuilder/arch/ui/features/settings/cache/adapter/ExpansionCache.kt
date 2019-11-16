package com.r0adkll.deckbuilder.arch.ui.features.settings.cache.adapter

import com.ftinc.kit.recycler.RecyclerViewItem
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.CacheStatus

data class ExpansionCache(
    val expansion: Expansion,
    val cacheStatus: CacheStatus.Cached
) : RecyclerViewItem {

    val itemId: Long get() = expansion.code.hashCode().toLong()
    override val layoutId: Int get() = R.layout.item_manage_cache_expansion

    override fun isItemSame(new: RecyclerViewItem): Boolean = when (new) {
        is ExpansionCache -> new.expansion.code == expansion.code
        else -> false
    }

    override fun isContentSame(new: RecyclerViewItem): Boolean = when (new) {
        is ExpansionCache -> new == this
        else -> false
    }
}
