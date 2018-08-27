package com.r0adkll.deckbuilder.arch.ui.features.overview.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.ui.widgets.EvolutionChainView


class OverviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val chainView = itemView as EvolutionChainView


    fun bind(evolutionChain: EvolutionChain) {
        chainView.evolutionChain = evolutionChain
    }


    companion object {

        fun create(inflater: LayoutInflater, parent: ViewGroup): OverviewViewHolder {
            return OverviewViewHolder(inflater.inflate(R.layout.item_overview, parent, false))
        }
    }
}