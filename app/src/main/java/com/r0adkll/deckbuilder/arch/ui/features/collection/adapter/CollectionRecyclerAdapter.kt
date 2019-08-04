package com.r0adkll.deckbuilder.arch.ui.features.collection.adapter

import android.content.Context
import android.view.ViewGroup
import com.r0adkll.deckbuilder.arch.ui.components.ListRecyclerAdapter


class CollectionRecyclerAdapter(context: Context) : ListRecyclerAdapter<Item, UiViewHolder<Item>>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UiViewHolder<Item> {
        val itemView = inflater.inflate(viewType, parent, false)
        return UiViewHolder.create(itemView, viewType)
    }

    override fun onBindViewHolder(vh: UiViewHolder<Item>, i: Int) {
        val item = items[i]
        if (item is Item.ExpansionSet) {
            super.onBindViewHolder(vh, i)
        }
        vh.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].viewType
    }

    fun setCollectionItems(newItems: List<Item>) {
        val diff = calculateDiff(newItems, items)
        items.clear()
        items.addAll(diff.new)
        diff.diff.dispatchUpdatesTo(getListUpdateCallback())
    }
}