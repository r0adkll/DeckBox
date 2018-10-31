package com.r0adkll.deckbuilder.arch.ui.features.browser.adapter

import com.r0adkll.deckbuilder.R
import com.ftinc.kit.kotlin.adapter.RecyclerItem
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.CacheStatus


sealed class Item : RecyclerItem {

    data class ExpansionSet(val expansion: Expansion, val offlineStatus: CacheStatus?) : Item() {

        override val layoutId: Int = R.layout.item_expansion

        override fun isItemSame(new: RecyclerItem): Boolean = when(new) {
            is ExpansionSet -> new.expansion.code == expansion.code
            else -> false
        }

        override fun isContentSame(new: RecyclerItem): Boolean = when(new) {
            is ExpansionSet -> new.expansion == expansion && new.offlineStatus == offlineStatus
            else -> false
        }
    }
}