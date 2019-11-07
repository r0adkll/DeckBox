package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftinc.kit.recycler.EmptyViewListAdapter
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerViewItemCallback

class DeckImageRecyclerAdapter(
    context: Context,
    private val itemClickListener: (DeckImage) -> Unit = {}
) : EmptyViewListAdapter<DeckImage, UiViewHolder<DeckImage>>(RecyclerViewItemCallback()) {

    private val inflater = LayoutInflater.from(context)

    var selectedDeckImage: DeckImage? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UiViewHolder<DeckImage> {
        val itemView = inflater.inflate(viewType, parent, false)
        return UiViewHolder.create(itemView, viewType)
    }

    override fun onBindViewHolder(vh: UiViewHolder<DeckImage>, i: Int) {
        val item = getItem(i)
        vh.bind(item, selectedDeckImage?.let { item.isItemSame(it) })
        vh.itemView.setOnClickListener {
            itemClickListener(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }
}
