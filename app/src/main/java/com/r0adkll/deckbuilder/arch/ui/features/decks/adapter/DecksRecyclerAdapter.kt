package com.r0adkll.deckbuilder.arch.ui.features.decks.adapter


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.components.ListRecyclerAdapter


class DecksRecyclerAdapter(
        context: Context,
        private val shareClicks: Relay<Deck>,
        private val duplicateClicks: Relay<Deck>,
        private val deleteClicks: Relay<Deck>,
        private val dismissPreview: Relay<Unit>,
        private val viewPreview: Relay<Unit>
) : ListRecyclerAdapter<Item, UiViewHolder<Item>>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UiViewHolder<Item> {
        val itemView = inflater.inflate(viewType, parent, false)
        return UiViewHolder.create(itemView, viewType, shareClicks, duplicateClicks, deleteClicks,
                dismissPreview, viewPreview)
    }


    override fun onBindViewHolder(vh: UiViewHolder<Item>, i: Int) {
        val item = items[i]
        if (item is Item.DeckItem) {
            super.onBindViewHolder(vh, i)
        }
        vh.bind(item)
    }


    override fun getItemViewType(position: Int): Int {
        if (position != RecyclerView.NO_POSITION) {
            return items[position].viewType
        }
        return super.getItemViewType(position)
    }


    fun showItems(decks: List<Item>) {
        val diff = calculateDiff(decks, items)
        items = ArrayList(diff.new)
        diff.diff.dispatchUpdatesTo(getListUpdateCallback())
    }
}