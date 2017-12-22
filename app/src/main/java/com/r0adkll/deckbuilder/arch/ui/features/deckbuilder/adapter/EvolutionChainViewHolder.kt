package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter


import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.EditCardIntentions
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.line.EvolutionLineRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.util.bindView


class EvolutionChainViewHolder(
        itemView: View,
        private val editCardIntentions: EditCardIntentions,
        private val pokemonCardClicks: Relay<PokemonCardView>
): RecyclerView.ViewHolder(itemView) {

    private val recyclerView: RecyclerView by bindView(R.id.recycler)


    fun bind(evolutionChain: EvolutionChain, isEditing: Boolean) {

        if (recyclerView.adapter == null) {
            val adapter = EvolutionLineRecyclerAdapter(itemView.context, editCardIntentions)
            adapter.setOnPokemonCardViewClickListener { pokemonCardClicks.accept(it) }
            recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = adapter
        }

        val adapter = recyclerView.adapter as EvolutionLineRecyclerAdapter
        adapter.evolution = evolutionChain
        adapter.isEditing = isEditing
        adapter.notifyDataSetChanged()
    }


    companion object {

        fun create(inflater: LayoutInflater,
                   parent: ViewGroup?,
                   editCardIntentions: EditCardIntentions,
                   pokemonCardClicks: Relay<PokemonCardView>
        ): EvolutionChainViewHolder {
            return EvolutionChainViewHolder(inflater.inflate(R.layout.item_evolution_chain, parent, false),
                    editCardIntentions, pokemonCardClicks)
        }
    }
}