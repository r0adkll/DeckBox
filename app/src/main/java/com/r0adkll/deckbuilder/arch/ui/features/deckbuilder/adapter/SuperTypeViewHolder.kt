package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter


import android.support.v7.widget.RecyclerView
import android.view.View
import com.ftinc.kit.widget.EmptyView
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.PokemonCard


class SuperTypeViewHolder(itemView: View) {

    private val recycler: RecyclerView = itemView.findViewById(R.id.recycler)
    private val emptyView: EmptyView = itemView.findViewById(R.id.empty_view)

    init {
        // Setup adapter
        // Setup recycler
    }

    fun bind(cards: List<PokemonCard>) {
        // Set cards to adapter

    }
}