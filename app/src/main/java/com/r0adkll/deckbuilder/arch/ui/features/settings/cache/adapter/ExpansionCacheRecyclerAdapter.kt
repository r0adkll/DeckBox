package com.r0adkll.deckbuilder.arch.ui.features.settings.cache.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftinc.kit.recycler.EmptyViewListAdapter
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerViewItemCallback

class ExpansionCacheRecyclerAdapter(
    context: Context,
    private val deleteClicks: Relay<Expansion>
) : EmptyViewListAdapter<ExpansionCache, ExpansionCacheViewHolder>(RecyclerViewItemCallback()) {

    private val inflater = LayoutInflater.from(context)

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpansionCacheViewHolder {
        val itemView = inflater.inflate(viewType, parent, false)
        return ExpansionCacheViewHolder(itemView, deleteClicks)
    }

    override fun onBindViewHolder(holder: ExpansionCacheViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).itemId
    }
}
