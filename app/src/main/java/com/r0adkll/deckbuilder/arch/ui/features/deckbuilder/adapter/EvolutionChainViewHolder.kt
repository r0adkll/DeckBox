package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.r0adkll.deckbuilder.R


class EvolutionChainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {



    fun bind(evolutionChain: EvolutionChain) {

    }


    companion object {

        fun create(inflater: LayoutInflater, parent: ViewGroup?): EvolutionChainViewHolder {
            return EvolutionChainViewHolder(inflater.inflate(R.layout.item_evolution_chain, parent, false))
        }
    }
}