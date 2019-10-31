package com.r0adkll.deckbuilder.arch.ui.components

import androidx.recyclerview.widget.DiffUtil
import com.ftinc.kit.recycler.RecyclerViewItem

class RecyclerViewItemCallback<T : RecyclerViewItem> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.isItemSame(newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.isContentSame(newItem)
    }
}
