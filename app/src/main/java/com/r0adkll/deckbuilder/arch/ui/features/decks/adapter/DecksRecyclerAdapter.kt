package com.r0adkll.deckbuilder.arch.ui.features.decks.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.ExpansionPreview
import com.r0adkll.deckbuilder.arch.ui.components.EmptyViewListAdapter


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
) : EmptyViewListAdapter<Item, UiViewHolder<Item>>(ITEM_CALLBACK) {

    var itemClickListener: (Item) -> Unit = {}
    private val inflater = LayoutInflater.from(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UiViewHolder<Item> {
        val itemView = inflater.inflate(viewType, parent, false)
        return UiViewHolder.create(itemView, viewType, shareClicks, duplicateClicks, testClicks,
                deleteClicks, dismissPreview, viewPreview, quickStart, dismissQuickStart)
    }


    override fun onBindViewHolder(vh: UiViewHolder<Item>, i: Int) {
        val item = getItem(i)
        if (item is Item.DeckItem) {
            vh.itemView.setOnClickListener {
                itemClickListener(item)
            }
        }
        vh.bind(item)

        if (item is Item.Header) {
            (vh.itemView.layoutParams as? StaggeredGridLayoutManager.LayoutParams)?.apply {
                this.isFullSpan = true
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        if (position != RecyclerView.NO_POSITION) {
            return getItem(position).viewType
        }
        return super.getItemViewType(position)
    }


    override fun getItemId(position: Int): Long {
        val item = getItem(position)
        return when(item) {
            is Item.DeckItem -> item.validatedDeck.deck.id.hashCode().toLong()
            is Item.QuickStart -> 0L
            is Item.Preview -> 1L
            is Item.Header -> item.text.hashCode().toLong()
        }
    }


    override fun onViewDetachedFromWindow(holder: UiViewHolder<Item>) {
        super.onViewDetachedFromWindow(holder)
        holder.dispose()
    }

    companion object {

        val ITEM_CALLBACK = object : DiffUtil.ItemCallback<Item>() {

            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.isItemSame(newItem)
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.isContentSame(newItem)
            }
        }
    }
}