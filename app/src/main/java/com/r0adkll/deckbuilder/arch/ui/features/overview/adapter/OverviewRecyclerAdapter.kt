package com.r0adkll.deckbuilder.arch.ui.features.overview.adapter

import android.content.Context
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.ui.components.EditCardIntentions
import com.r0adkll.deckbuilder.arch.ui.components.ListRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView


class OverviewRecyclerAdapter(
        context: Context,
        private val cardClicks: Relay<PokemonCardView>,
        private val editCardIntentions: EditCardIntentions
) : ListRecyclerAdapter<EvolutionChain, OverviewViewHolder>(context) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverviewViewHolder {
        return OverviewViewHolder.create(inflater, parent, cardClicks, editCardIntentions)
    }


    override fun onBindViewHolder(vh: OverviewViewHolder, i: Int) {
        vh.bind(items[i])
    }


    fun setCards(cards: List<EvolutionChain>) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = items.size
            override fun getNewListSize(): Int = cards.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = items[oldItemPosition]
                val newItem = cards[newItemPosition]
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldItem = items[oldItemPosition]
                val newItem = cards[newItemPosition]
                return oldItem.nodes == newItem.nodes
            }
        })

        items = ArrayList(cards)
        diff.dispatchUpdatesTo(getListUpdateCallback())
    }
}