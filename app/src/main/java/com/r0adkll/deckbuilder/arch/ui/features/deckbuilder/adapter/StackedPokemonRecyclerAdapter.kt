package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter


import android.content.Context
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.ListRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerViewBinding
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.PokemonCardViewHolder


class StackedPokemonRecyclerAdapter(
        context: Context
) : ListRecyclerAdapter<StackedPokemonCard, PokemonCardViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonCardViewHolder {
        return PokemonCardViewHolder.create(inflater, parent, false)
    }


    override fun onBindViewHolder(vh: PokemonCardViewHolder, i: Int) {
        super.onBindViewHolder(vh, i)
        val card = items[i]
        vh.bind(card.card, card.count)
    }


    fun setCards(cards: List<StackedPokemonCard>) {
        val diff = calculateDiff(items, cards)
        items = ArrayList(diff.new)
        diff.diff.dispatchUpdatesTo(DiffUpdateCallback())
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