package com.r0adkll.deckbuilder.arch.ui.features.playtest.adapter

import android.content.Context
import android.view.ViewGroup
import com.ftinc.kit.kotlin.adapter.ListRecyclerAdapter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard


class HandRecyclerAdapter(context: Context) : ListRecyclerAdapter<PokemonCard, HandViewHolder>(context) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HandViewHolder {
        TODO()
    }


    override fun onBindViewHolder(vh: HandViewHolder, i: Int) {
        super.onBindViewHolder(vh, i)

    }

    fun setHand(cards: List<PokemonCard>) {

    }
}