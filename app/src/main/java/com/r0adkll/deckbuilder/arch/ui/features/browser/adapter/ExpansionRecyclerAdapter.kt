package com.r0adkll.deckbuilder.arch.ui.features.browser.adapter

import android.content.Context
import android.view.ViewGroup
import com.ftinc.kit.kotlin.adapter.ListRecyclerAdapter
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion


class ExpansionRecyclerAdapter(
        context: Context,
        private val downloadClicks: Relay<Expansion>
) : ListRecyclerAdapter<Item, UiViewHolder<Item>>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UiViewHolder<Item> {
        val itemView = inflater.inflate(viewType, parent, false)
        return UiViewHolder.create(itemView, viewType, downloadClicks)
    }

    override fun onBindViewHolder(vh: UiViewHolder<Item>, i: Int) {
        super.onBindViewHolder(vh, i)
        vh.bind(items[i])
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].viewType
    }

    fun setExpansionItems(newItems: List<Item>) {
        val diff = calculateDiff(newItems, items)
        items = ArrayList(diff.new)
        diff.diff.dispatchUpdatesTo(getListUpdateCallback())
    }
}