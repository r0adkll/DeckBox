package com.r0adkll.deckbuilder.arch.ui.features.collection.adapter

import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerItem


sealed class Item : RecyclerItem {

    data class Migration(val isLoading: Boolean, val error: String?) : Item() {

        override val layoutId: Int get() = R.layout.item_collection_migration

        override fun isItemSame(new: RecyclerItem): Boolean = new is Migration

        override fun isContentSame(new: RecyclerItem): Boolean = when(new) {
            is Migration -> new.isLoading == isLoading && new.error == error
            else -> false
        }
    }

    data class ExpansionSeries(val series: String, val completion: Float) : Item() {

        override val layoutId: Int get() = R.layout.item_collection_series

        override fun isItemSame(new: RecyclerItem): Boolean = when(new) {
            is ExpansionSeries -> new.series == series
            else -> false
        }

        override fun isContentSame(new: RecyclerItem): Boolean = when(new) {
            is ExpansionSeries -> new.series == series && new.completion == completion
            else -> false
        }
    }

    data class ExpansionSet(val expansion: Expansion, val count: Int) : Item() {

        override val layoutId: Int get() = R.layout.item_collection_expansion

        override fun isItemSame(new: RecyclerItem): Boolean = when(new) {
            is ExpansionSet -> new.expansion.code == expansion.code
            else -> false
        }

        override fun isContentSame(new: RecyclerItem): Boolean = when(new) {
            is ExpansionSet -> new.expansion.code == expansion.code && new.count == count
            else -> false
        }
    }
}