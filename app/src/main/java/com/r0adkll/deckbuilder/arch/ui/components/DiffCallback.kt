package com.r0adkll.deckbuilder.arch.ui.components


import androidx.recyclerview.widget.DiffUtil


class DiffCallback<out T : RecyclerItem>(
        private val oldItems: List<T>,
        private val newItems: List<T>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size
    override fun getNewListSize(): Int = newItems.size


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem.isContentSame(newItem)
    }


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem.isItemSame(newItem)
    }
}