package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.widgets.EvolutionChainView
import com.r0adkll.deckbuilder.util.bindView


class EvolutionChainViewHolder(
        itemView: View,
        private val pokemonCardClicks: Relay<PokemonCard>
): RecyclerView.ViewHolder(itemView) {

    private val evolutionView: EvolutionChainView by bindView(R.id.evolution_view)


    fun bind(evolutionChain: EvolutionChain) {
        evolutionView.evolutionChain = evolutionChain
        evolutionView.setOnPokemonCardClickListener { pokemonCardClicks.accept(it) }
    }


    companion object {

        fun create(inflater: LayoutInflater,
                   parent: ViewGroup?,
                   pokemonCardClicks: Relay<PokemonCard>): EvolutionChainViewHolder {
            return EvolutionChainViewHolder(inflater.inflate(R.layout.item_evolution_chain, parent, false), pokemonCardClicks)
        }
    }
}