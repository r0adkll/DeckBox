package com.r0adkll.deckbuilder.arch.ui.features.decks.adapter


import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.ExpansionPreview
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.components.ListRecyclerAdapter


class DecksRecyclerAdapter(
        context: Context,
        private val shareClicks: Relay<Deck>,
        private val duplicateClicks: Relay<Deck>,
        private val deleteClicks: Relay<Deck>,
        private val testClicks: Relay<Deck>,
        private val dismissPreview: Relay<Unit>,
        private val viewPreview: Relay<ExpansionPreview>,
        private val quickStart: Relay<Deck>,
        private val dismissQuickStart: Relay<Unit>
) : ListRecyclerAdapter<Item, UiViewHolder<Item>>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UiViewHolder<Item> {
        val itemView = inflater.inflate(viewType, parent, false)
        return UiViewHolder.create(itemView, viewType, shareClicks, duplicateClicks, testClicks,
                deleteClicks, dismissPreview, viewPreview, quickStart, dismissQuickStart)
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


    override fun getItemId(position: Int): Long {
        val item = items[position]
        return when(item) {
            is Item.DeckItem -> item.deck.id.hashCode().toLong()
            is Item.QuickStart -> 0L
            is Item.Preview -> 1L
        }
    }


    override fun onViewDetachedFromWindow(holder: UiViewHolder<Item>) {
        super.onViewDetachedFromWindow(holder)
        holder.dispose()
    }


    fun showItems(decks: List<Item>) {
        val diff = calculateDiff(decks, items)
        items = ArrayList(diff.new)
        diff.diff.dispatchUpdatesTo(getListUpdateCallback())
    }
}