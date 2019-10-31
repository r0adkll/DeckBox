package com.r0adkll.deckbuilder.arch.ui.features.collection.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftinc.kit.recycler.EmptyViewListAdapter
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerViewItemCallback

class CollectionRecyclerAdapter(
        context: Context,
        private val migrateClicks: Relay<Unit>,
        private val dismissClicks: () -> Unit,
        private val onItemClickListener: (Item) -> Unit = { }
) : EmptyViewListAdapter<Item, UiViewHolder<Item>>(RecyclerViewItemCallback()) {

    private val inflater = LayoutInflater.from(context)

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UiViewHolder<Item> {
        val itemView = inflater.inflate(viewType, parent, false)
        return UiViewHolder.create(itemView, viewType, migrateClicks, dismissClicks)
    }

    override fun onBindViewHolder(vh: UiViewHolder<Item>, i: Int) {
        val item = getItem(i)
        if (item is Item.ExpansionSet) {
            vh.itemView.setOnClickListener {
                onItemClickListener(item)
            }
        } else {
            vh.itemView.setOnClickListener(null)
        }
        vh.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).itemId
    }
}
