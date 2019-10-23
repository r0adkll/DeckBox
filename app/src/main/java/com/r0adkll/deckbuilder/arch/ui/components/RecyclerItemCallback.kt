package com.r0adkll.deckbuilder.arch.ui.components

import androidx.recyclerview.widget.DiffUtil

class RecyclerItemCallback<T : RecyclerItem> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.isItemSame(newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.isContentSame(newItem)
    }
}
