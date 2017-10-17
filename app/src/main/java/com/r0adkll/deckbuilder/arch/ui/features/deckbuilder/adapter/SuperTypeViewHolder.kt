package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter


import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.View
import com.ftinc.kit.widget.EmptyView
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard


class SuperTypeViewHolder(
        itemView: View,
        pokemonCardClicks: Relay<PokemonCard>
) {

    private val recycler: RecyclerView = itemView.findViewById(R.id.recycler)
    private val emptyView: EmptyView = itemView.findViewById(R.id.empty_view)

    private var adapter: EvolutionChainRecyclerAdapter = EvolutionChainRecyclerAdapter(itemView.context, pokemonCardClicks)

    init {
        // Setup adapter
        adapter.setEmptyView(emptyView)

        // Setup recycler
        recycler.layoutManager = LinearLayoutManager(itemView.context)
        recycler.adapter = adapter
        (recycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    fun bind(cards: List<PokemonCard>) {
        // Set cards to adapter
        val evolutionChains = EvolutionChain.build(cards)
        adapter.setEvolutions(evolutionChains)
    }
}