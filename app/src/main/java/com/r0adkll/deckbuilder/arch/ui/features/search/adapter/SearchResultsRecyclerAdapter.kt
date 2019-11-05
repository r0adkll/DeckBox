package com.r0adkll.deckbuilder.arch.ui.features.search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ftinc.kit.recycler.EmptyViewListAdapter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.EditCardIntentions

class SearchResultsRecyclerAdapter(
    context: Context,
    val instantDragSupport: Boolean = false,
    val editCardIntentions: EditCardIntentions = EditCardIntentions()
) : EmptyViewListAdapter<PokemonCard, PokemonCardViewHolder>(ITEM_CALLBACK) {

    private val inflater = LayoutInflater.from(context)
    private var selectedCards: List<PokemonCard> = emptyList()

    var onItemClickListener: (View, PokemonCard) -> Unit = { _, _ -> }
    var onItemLongClickListener: (View, PokemonCard) -> Boolean = { _, _ -> false }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonCardViewHolder {
        return PokemonCardViewHolder.create(inflater, parent, true, instantDragSupport,
            editCardIntentions.removeCardClicks, editCardIntentions.addCardClicks)
    }

    override fun onBindViewHolder(vh: PokemonCardViewHolder, i: Int) {
        val card = getItem(i)
        val count = selectedCards.count { it.id == card.id }
        vh.bind(card.stacked(count), isEditMode = true)

        vh.itemView.setOnClickListener {
            onItemClickListener(it, card)
        }

        vh.itemView.setOnLongClickListener {
            onItemLongClickListener(it, card)
        }
    }

    fun setSelectedCards(cards: List<PokemonCard>) {
        selectedCards = cards
        notifyDataSetChanged()
    }

    fun indexOf(card: PokemonCard): Int {
        return currentList.indexOf(card)
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
