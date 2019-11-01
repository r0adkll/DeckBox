package com.r0adkll.deckbuilder.arch.ui.features.filter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ftinc.kit.recycler.EmptyViewListAdapter
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerViewItemCallback
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterIntentions

class FilterRecyclerAdapter(
    context: Context,
    private val intentions: FilterIntentions
) : EmptyViewListAdapter<Item, UiViewHolder<Item>>(RecyclerViewItemCallback()) {

    private val inflater = LayoutInflater.from(context)

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UiViewHolder<Item> {
        val itemView = inflater.inflate(viewType, parent, false)
        return UiViewHolder.create(itemView, viewType, intentions)
    }

    override fun onBindViewHolder(vh: UiViewHolder<Item>, i: Int) {
        vh.bind(getItem(i))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    override fun getItemId(position: Int): Long {
        if (position > -1 && position < itemCount) {
            return getItem(position).itemId
        }
        return RecyclerView.NO_ID
    }
}
