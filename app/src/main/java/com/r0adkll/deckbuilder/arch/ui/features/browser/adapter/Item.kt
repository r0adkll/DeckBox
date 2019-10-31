package com.r0adkll.deckbuilder.arch.ui.features.browser.adapter

import com.ftinc.kit.recycler.RecyclerViewItem
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.CacheStatus

sealed class Item : RecyclerViewItem {

    abstract val itemId: Long

    object OfflineOutline : Item() {

        override val layoutId: Int get() = R.layout.item_expansion_outline
        override val itemId: Long get() = 0L

        override fun isItemSame(new: RecyclerViewItem): Boolean = new is OfflineOutline

        override fun isContentSame(new: RecyclerViewItem): Boolean = new is OfflineOutline
    }

    data class ExpansionSet(
            val expansion: Expansion,
            val offlineStatus: CacheStatus?
    ) : Item() {

        override val layoutId: Int get() = R.layout.item_expansion
        override val itemId: Long get() = expansion.code.hashCode().toLong()

        override fun isItemSame(new: RecyclerViewItem): Boolean = when(new) {
            is ExpansionSet -> new.expansion.code == expansion.code
            else -> false
        }

        override fun isContentSame(new: RecyclerViewItem): Boolean = when(new) {
            is ExpansionSet -> new.expansion == expansion && new.offlineStatus == offlineStatus
            else -> false
        }
    }
}
