package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter


import android.content.Context
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.ListRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerViewBinding


class EvolutionChainRecyclerAdapter(
        context: Context,
        private val pokemonCardClicks: Relay<PokemonCard>
) : ListRecyclerAdapter<EvolutionChain, EvolutionChainViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EvolutionChainViewHolder {
        return EvolutionChainViewHolder.create(inflater, parent, pokemonCardClicks)
    }


    override fun onBindViewHolder(vh: EvolutionChainViewHolder, i: Int) {
        vh.bind(items[i])
    }


    fun setEvolutions(evolutions: List<EvolutionChain>) {
        val diff = calculateDiff(items, evolutions)
        items = ArrayList(diff.new)
        diff.diff.dispatchUpdatesTo(DiffUpdateCallback())
    }


    companion object {
        fun calculateDiff(old: List<EvolutionChain>, new: List<EvolutionChain>): RecyclerViewBinding<EvolutionChain> {
            val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = old[oldItemPosition]
                    val newItem = new[newItemPosition]
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = old[oldItemPosition]
                    val newItem = new[newItemPosition]
                    return oldItem == newItem
                }

                override fun getOldListSize(): Int = old.size
                override fun getNewListSize(): Int = new.size
            })

            return RecyclerViewBinding(new = new, diff = diff)
        }
    }
}