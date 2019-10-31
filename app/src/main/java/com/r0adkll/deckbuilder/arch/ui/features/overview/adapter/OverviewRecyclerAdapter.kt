package com.r0adkll.deckbuilder.arch.ui.features.overview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ftinc.kit.recycler.EmptyViewListAdapter
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.ui.components.EditCardIntentions
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView

class OverviewRecyclerAdapter(
        context: Context,
        private val cardClicks: Relay<PokemonCardView>,
        private val editCardIntentions: EditCardIntentions
) : EmptyViewListAdapter<EvolutionChain, OverviewViewHolder>(ITEM_CALLBACK) {

    private val inflater = LayoutInflater.from(context)

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverviewViewHolder {
        return OverviewViewHolder.create(inflater, parent, cardClicks, editCardIntentions)
    }

    override fun onBindViewHolder(vh: OverviewViewHolder, i: Int) {
        vh.bind(getItem(i))
    }

    override fun getItemId(position: Int): Long {
        return if (position != RecyclerView.NO_POSITION) {
            getItem(position).id.hashCode().toLong()
        } else RecyclerView.NO_ID
    }

    companion object {

        val ITEM_CALLBACK = object : DiffUtil.ItemCallback<EvolutionChain>() {

            override fun areItemsTheSame(oldItem: EvolutionChain, newItem: EvolutionChain): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: EvolutionChain, newItem: EvolutionChain): Boolean {
                return oldItem.nodes == newItem.nodes
            }
        }
    }
}
