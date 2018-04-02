package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter


import android.content.Context
import android.view.ViewGroup
import com.r0adkll.deckbuilder.arch.ui.components.ListRecyclerAdapter


class DeckImageRecyclerAdapter(
        context: Context
) : ListRecyclerAdapter<DeckImage, UiViewHolder<DeckImage>>(context) {

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
        super.onBindViewHolder(vh, i)
        val item = items[i]
        vh.bind(item, selectedDeckImage?.let { item.isItemSame(it) })
    }


    override fun getItemViewType(position: Int): Int {
        return items[position].viewType
    }


    fun setDeckImages(images: List<DeckImage>) {
        val diff = calculateDiff(images, items)
        items.clear()
        items.addAll(diff.new)
        diff.diff.dispatchUpdatesTo(getListUpdateCallback())
    }
}