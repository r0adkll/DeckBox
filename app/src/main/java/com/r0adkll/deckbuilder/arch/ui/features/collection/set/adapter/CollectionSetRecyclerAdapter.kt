package com.r0adkll.deckbuilder.arch.ui.features.collection.set.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.ListRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerViewBinding


class CollectionSetRecyclerAdapter(
        context: Context,
        private val removeCardClicks: Relay<PokemonCard>,
        private val addCardClicks: Relay<List<PokemonCard>>
): ListRecyclerAdapter<StackedPokemonCard, CollectionCardViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionCardViewHolder {
        return CollectionCardViewHolder.create(inflater, parent, removeCardClicks, addCardClicks)
    }

    override fun onBindViewHolder(vh: CollectionCardViewHolder, i: Int) {
        super.onBindViewHolder(vh, i)
        val item = items[i]
        vh.bind(item.card, item.count)
    }

    fun setCollectionItems(newItems: List<StackedPokemonCard>) {
        val diff = calculateDiff(newItems, items)
        items = ArrayList(diff.new)
        diff.diff.dispatchUpdatesTo(getListUpdateCallback())
    }

    companion object {

        private fun calculateDiff(old: List<StackedPokemonCard>, new: List<StackedPokemonCard>): RecyclerViewBinding<StackedPokemonCard> {
            val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = old[oldItemPosition]
                    val newItem = new[newItemPosition]
                    return oldItem.card.id == newItem.card.id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = old[oldItemPosition]
                    val newItem = new[newItemPosition]
                    return oldItem.hashCode() == newItem.hashCode()
                }

                override fun getOldListSize(): Int = old.size
                override fun getNewListSize(): Int = new.size
            })

            return RecyclerViewBinding(new = new, diff = diff)
        }
    }
}