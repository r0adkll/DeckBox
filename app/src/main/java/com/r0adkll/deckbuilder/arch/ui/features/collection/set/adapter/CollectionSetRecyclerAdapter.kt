package com.r0adkll.deckbuilder.arch.ui.features.collection.set.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.EmptyViewListAdapter
import com.r0adkll.deckbuilder.arch.ui.components.StackedPokemonCardItemCallback

class CollectionSetRecyclerAdapter(
        context: Context,
        private val removeCardClicks: Relay<PokemonCard>,
        private val addCardClicks: Relay<List<PokemonCard>>,
        private val itemClickListener: (StackedPokemonCard) -> Unit = { },
        private val itemLongClickListener: (View, StackedPokemonCard) -> Boolean = { _, _ -> false }
): EmptyViewListAdapter<StackedPokemonCard, CollectionCardViewHolder>(StackedPokemonCardItemCallback()) {

    private val inflater = LayoutInflater.from(context)

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionCardViewHolder {
        return CollectionCardViewHolder.create(inflater, parent, removeCardClicks, addCardClicks)
    }

    override fun onBindViewHolder(vh: CollectionCardViewHolder, i: Int) {
        val item = getItem(i)
        vh.bind(item.card, item.count)
        vh.itemView.setOnClickListener {
            itemClickListener(item)
        }
        vh.itemView.setOnLongClickListener {
            itemLongClickListener(it, item)
        }
    }

    override fun getItemId(position: Int): Long {
        if (position != RecyclerView.NO_POSITION) {
            val item = getItem(position)
            return item.card.id.hashCode().toLong()
        }
        return super.getItemId(position)
    }
}
