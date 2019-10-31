package com.r0adkll.deckbuilder.arch.ui.features.carddetail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ftinc.kit.recycler.EmptyViewListAdapter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.PokemonCardViewHolder
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView

class PokemonCardsRecyclerAdapter(
        context: Context,
        private val onViewItemClickListener: (PokemonCardView, PokemonCard) -> Unit = { _, _ -> }
) : EmptyViewListAdapter<PokemonCard, PokemonCardViewHolder>(ITEM_CALLBACK) {

    private val inflater = LayoutInflater.from(context)

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonCardViewHolder {
        return PokemonCardViewHolder.createHorizontal(inflater, parent, false)
    }

    override fun onBindViewHolder(vh: PokemonCardViewHolder, i: Int) {
        val card = getItem(i)
        vh.bind(card, 0)
        vh.cardView.setOnClickListener {
            onViewItemClickListener(it as PokemonCardView, card)
        }
    }

    override fun getItemId(position: Int): Long {
        return if (position != RecyclerView.NO_POSITION) {
            getItem(position).id.hashCode().toLong()
        } else RecyclerView.NO_ID
    }

    companion object {

        val ITEM_CALLBACK = object : DiffUtil.ItemCallback<PokemonCard>() {

            override fun areItemsTheSame(oldItem: PokemonCard, newItem: PokemonCard): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PokemonCard, newItem: PokemonCard): Boolean {
                return oldItem == newItem
            }
        }
    }
}
