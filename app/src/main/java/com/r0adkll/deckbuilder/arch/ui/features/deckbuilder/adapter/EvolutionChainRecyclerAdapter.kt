package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter

import android.content.Context
import android.view.ViewGroup
import com.r0adkll.deckbuilder.arch.ui.components.ListRecyclerAdapter


class EvolutionChainRecyclerAdapter(
        context: Context
) : ListRecyclerAdapter<EvolutionChain, EvolutionChainViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EvolutionChainViewHolder {
        TODO()
    }


    override fun onBindViewHolder(vh: EvolutionChainViewHolder, i: Int) {
        vh.bind(items[i])
    }
}