package com.r0adkll.deckbuilder.arch.ui.features.settings.cache.adapter

import com.ftinc.kit.recycler.RecyclerViewItem
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R

sealed class Item : RecyclerViewItem {

    abstract val itemId: Long

    data class CacheSizeHeader(
        val sizeInBytes: Long,
        val cachePath: String
    ) : Item() {

        override val itemId: Long get() = 0L
        override val layoutId: Int get() = R.layout.item_manage_cache_size_header

        override fun isItemSame(new: RecyclerViewItem): Boolean {
            return new is CacheSizeHeader
        }

        override fun isContentSame(new: RecyclerViewItem): Boolean = when (new) {
            is CacheSizeHeader -> new == this
            else -> false
        }
    }
}
