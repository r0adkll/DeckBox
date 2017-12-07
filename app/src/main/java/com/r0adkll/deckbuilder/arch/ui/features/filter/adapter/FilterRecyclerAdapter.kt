package com.r0adkll.deckbuilder.arch.ui.features.filter.adapter


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.r0adkll.deckbuilder.arch.ui.components.ListRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.filter.FilterIntentions


class FilterRecyclerAdapter(
        context: Context,
        private val intentions: FilterIntentions
) : ListRecyclerAdapter<Item, UiViewHolder<Item>>(context) {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UiViewHolder<Item> {
        val itemView = inflater.inflate(viewType, parent, false)
        return UiViewHolder.create(itemView, viewType, intentions)
    }


    override fun onBindViewHolder(vh: UiViewHolder<Item>, i: Int) {
        vh.bind(items[i])
    }


    override fun getItemViewType(position: Int): Int {
        return items[position].viewType
    }


    override fun getItemId(position: Int): Long {
        if (position > -1 && position < itemCount) {
            return items[position].itemId
        }
        return RecyclerView.NO_ID
    }


    fun setFilterItems(items: List<Item>) {
        val diff = calculateDiff(items, this.items)
        this.items = ArrayList(diff.new)
        diff.diff.dispatchUpdatesTo(this)
    }
}