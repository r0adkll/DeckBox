package com.r0adkll.deckbuilder.arch.ui.features.browser.adapter

import android.content.Context
import android.view.ViewGroup
import com.ftinc.kit.kotlin.adapter.ListRecyclerAdapter
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.domain.Format
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion

class ExpansionRecyclerAdapter(
        context: Context,
        private val downloadClicks: Relay<Expansion>,
        private val dismissClicks: Relay<Unit>,
        private val downloadFormat: Relay<Format>
) : ListRecyclerAdapter<Item, UiViewHolder<Item>>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UiViewHolder<Item> {
        val itemView = inflater.inflate(viewType, parent, false)
        return UiViewHolder.create(itemView, viewType, downloadClicks, dismissClicks, downloadFormat)
    }

    override fun onBindViewHolder(vh: UiViewHolder<Item>, i: Int) {
        super.onBindViewHolder(vh, i)
        vh.bind(items[i])
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].viewType
    }

    override fun getItemId(position: Int): Long {
        val item = items[position]
        return when(item) {
            is Item.ExpansionSet -> item.expansion.code.hashCode().toLong()
            else -> 0L
        }
    }

    fun setExpansionItems(newItems: List<Item>) {
        val diff = calculateDiff(newItems, items)
        items = ArrayList(diff.new)
        diff.diff.dispatchUpdatesTo(getListUpdateCallback())
    }
}
