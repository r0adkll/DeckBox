package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.pageradapter


import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
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
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.EvolutionChainRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.StackedPokemonRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import io.pokemontcg.model.SubType


abstract class SuperTypeViewHolder<out A : ListRecyclerAdapter<*, *>>(
        itemView: View,
        @DrawableRes val emptyIcon: Int,
        @StringRes val emptyMessage: Int,
        val pokemonCardClicks: Relay<PokemonCardView>
) {

    protected val recycler: RecyclerView = itemView.findViewById(R.id.recycler)
    private val emptyView: EmptyView = itemView.findViewById(R.id.empty_view)

    abstract val adapter: A
    abstract val layoutManager: RecyclerView.LayoutManager
    abstract fun bind(cards: List<StackedPokemonCard>)


    fun setup() {
        emptyView.setIcon(emptyIcon)
        emptyView.setEmptyMessage(emptyMessage)

        adapter.setEmptyView(emptyView)
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter
        (recycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }
}


class PokemonViewHolder(
        itemView: View,
        emptyIcon: Int,
        emptyMessage: Int,
        pokemonCardClicks: Relay<PokemonCardView>
) : SuperTypeViewHolder<EvolutionChainRecyclerAdapter>(itemView, emptyIcon, emptyMessage, pokemonCardClicks) {
    override val adapter: EvolutionChainRecyclerAdapter = EvolutionChainRecyclerAdapter(itemView.context, pokemonCardClicks)
    override val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(itemView.context)

    override fun bind(cards: List<StackedPokemonCard>) {
        val evolutions = EvolutionChain.build(cards)
                .sortedByDescending { chain -> chain.nodes.size }
        adapter.setEvolutions(evolutions)
    }
}

class TrainerEnergyViewHolder(
        itemView: View,
        emptyIcon: Int,
        emptyMessage: Int,
        pokemonCardClicks: Relay<PokemonCardView>
) : SuperTypeViewHolder<StackedPokemonRecyclerAdapter>(itemView, emptyIcon, emptyMessage, pokemonCardClicks) {
    override val adapter: StackedPokemonRecyclerAdapter = StackedPokemonRecyclerAdapter(itemView.context)
    override val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(itemView.context, 3)

    init {
        recycler.setHasFixedSize(true)
    }

    override fun bind(cards: List<StackedPokemonCard>) {
        val sorted = cards.sortedBy { c ->
            when(c.card.subtype) {
                SubType.SUPPORTER -> 0
                SubType.ITEM -> 1
                SubType.POKEMON_TOOL -> 2
                SubType.STADIUM -> 3
                SubType.SPECIAL -> 4
                else -> 10
            }
        }
        adapter.setCards(sorted)
        adapter.setOnViewItemClickListener { view, _ ->
            pokemonCardClicks.accept(view as PokemonCardView)
        }
    }
}