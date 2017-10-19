package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.pageradapter


import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.View
import com.ftinc.kit.widget.EmptyView
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.ListRecyclerAdapter
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.EvolutionChainRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.SearchResultsRecyclerAdapter

abstract class SuperTypeViewHolder<out A : ListRecyclerAdapter<*, *>>(
        itemView: View,
        val pokemonCardClicks: Relay<PokemonCard>
) {

    private val recycler: RecyclerView = itemView.findViewById(R.id.recycler)
    private val emptyView: EmptyView = itemView.findViewById(R.id.empty_view)

    abstract val adapter: A
    abstract val layoutManager: RecyclerView.LayoutManager
    abstract fun bind(cards: List<PokemonCard>)


    fun setup() {
        adapter.setEmptyView(emptyView)
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter
        (recycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }
}


class PokemonViewHolder(
        itemView: View, pokemonCardClicks: Relay<PokemonCard>
) : SuperTypeViewHolder<EvolutionChainRecyclerAdapter>(itemView, pokemonCardClicks) {
    override val adapter: EvolutionChainRecyclerAdapter = EvolutionChainRecyclerAdapter(itemView.context, pokemonCardClicks)
    override val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(itemView.context)

    override fun bind(cards: List<PokemonCard>) {
        val evolutions = EvolutionChain.build(cards)
        adapter.setEvolutions(evolutions)
    }
}

class TrainerEnergyViewHolder(
        itemView: View, pokemonCardClicks: Relay<PokemonCard>
) : SuperTypeViewHolder<SearchResultsRecyclerAdapter>(itemView, pokemonCardClicks) {
    override val adapter: SearchResultsRecyclerAdapter = SearchResultsRecyclerAdapter(itemView.context)
    override val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(itemView.context, 3)

    override fun bind(cards: List<PokemonCard>) {
        adapter.setCards(cards)
    }
}