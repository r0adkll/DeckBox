package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.PokemonCardViewHolder


class EvolutionLineRecyclerAdapter(
        val context: Context
) : RecyclerView.Adapter<PokemonCardViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    var evolution: EvolutionChain? = null



    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PokemonCardViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onBindViewHolder(holder: PokemonCardViewHolder?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun getItemCount(): Int {
        return evolution?.size ?: 0
    }


    private fun getItem(position: Int): StackedPokemonCard? {
        return evolution?.nodes?.flatMap { it.cards }?.getOrNull(position)
    }


    private fun setEvolutionLine(line: EvolutionChain) {
        evolution = line
    }

}