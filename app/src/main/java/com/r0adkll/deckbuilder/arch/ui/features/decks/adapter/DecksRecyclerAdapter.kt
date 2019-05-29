package com.r0adkll.deckbuilder.arch.ui.features.decks.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.Format
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.ExpansionPreview
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.components.EmptyViewListAdapter
import com.r0adkll.deckbuilder.util.bindView
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter


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
) : EmptyViewListAdapter<Item, UiViewHolder<Item>>(ITEM_CALLBACK), StickyRecyclerHeadersAdapter<DecksRecyclerAdapter.HeaderViewHolder> {

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
            itemClickListener(item)
        }
        vh.bind(item)
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
        }
    }


    override fun onViewDetachedFromWindow(holder: UiViewHolder<Item>) {
        super.onViewDetachedFromWindow(holder)
        holder.dispose()
    }

    override fun getHeaderId(position: Int): Long {
        val item = getItem(position)
        return when(item) {
            is Item.DeckItem -> {

                TODO()
            }
            else -> RecyclerView.NO_ID
        }
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup?): HeaderViewHolder {
        val itemView = inflater.inflate(R.layout.layout_sticky_subheader, parent, false)
        return HeaderViewHolder(itemView)
    }

    override fun onBindHeaderViewHolder(holder: HeaderViewHolder, position: Int) {

    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title by bindView<TextView>(R.id.title)

        fun bind(format: Format) {

        }
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