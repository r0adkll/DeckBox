package com.r0adkll.deckbuilder.arch.ui.features.overview.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.ui.components.EditCardIntentions
import com.r0adkll.deckbuilder.arch.ui.widgets.EvolutionChainView
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView


class OverviewViewHolder(
        itemView: View,
        private val cardClicks: Relay<PokemonCardView>,
        private val editCardIntentions: EditCardIntentions
) : RecyclerView.ViewHolder(itemView) {

    private val chainView = itemView as EvolutionChainView


    fun bind(evolutionChain: EvolutionChain) {
        chainView.evolutionChain = evolutionChain
        chainView.setOnPokemonCardEditListener({
            editCardIntentions.addCardClicks.accept(listOf(it))
        }, {
            editCardIntentions.removeCardClicks.accept(it)
        })
        chainView.setOnPokemonCardClickListener {
            cardClicks.accept(it)
        }
    }


    companion object {

        fun create(inflater: LayoutInflater,
                   parent: ViewGroup,
                   cardClicks: Relay<PokemonCardView>,
                   editCardIntentions: EditCardIntentions): OverviewViewHolder {
            return OverviewViewHolder(inflater.inflate(R.layout.item_overview, parent, false), cardClicks, editCardIntentions)
        }
    }
}