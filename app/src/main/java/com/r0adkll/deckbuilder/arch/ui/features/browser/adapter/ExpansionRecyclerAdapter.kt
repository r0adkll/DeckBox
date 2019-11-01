package com.r0adkll.deckbuilder.arch.ui.features.browser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftinc.kit.recycler.EmptyViewListAdapter
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.domain.Format
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerViewItemCallback

class ExpansionRecyclerAdapter(
    context: Context,
    private val downloadClicks: Relay<Expansion>,
    private val dismissClicks: Relay<Unit>,
    private val downloadFormat: Relay<Format>,
    private val onItemClickListener: (Item) -> Unit
) : EmptyViewListAdapter<Item, UiViewHolder<Item>>(RecyclerViewItemCallback()) {

    private val inflater = LayoutInflater.from(context)

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UiViewHolder<Item> {
        val itemView = inflater.inflate(viewType, parent, false)
        return UiViewHolder.create(itemView, viewType, downloadClicks, dismissClicks, downloadFormat)
    }

    override fun onBindViewHolder(vh: UiViewHolder<Item>, i: Int) {
        val item = getItem(i)
        vh.bind(item)

        vh.itemView.setOnClickListener {
            onItemClickListener(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).itemId
    }
}
