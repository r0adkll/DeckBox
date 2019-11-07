package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.ftinc.kit.arch.util.bindView
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.ui.components.EditCardIntentions
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.line.EvolutionLineItemDecoration
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.line.EvolutionLineRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView

class EvolutionChainViewHolder(
    itemView: View,
    private val spanCount: Int,
    private val editCardIntentions: EditCardIntentions,
    private val pokemonCardClicks: Relay<PokemonCardView>
) : RecyclerView.ViewHolder(itemView) {

    private val recyclerView by bindView<RecyclerView>(R.id.recycler)

    fun bind(evolutionChain: EvolutionChain, isEditing: Boolean, isCollectionEnabled: Boolean) {

        if (recyclerView.adapter == null) {
            val adapter = EvolutionLineRecyclerAdapter(itemView.context, editCardIntentions, spanCount)
            adapter.setOnPokemonCardViewClickListener { pokemonCardClicks.accept(it) }
            recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = adapter
            (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            (0 until recyclerView.itemDecorationCount).forEach { recyclerView.removeItemDecorationAt(it) }
            recyclerView.addItemDecoration(EvolutionLineItemDecoration(itemView.context, adapter))
        }

        val adapter = recyclerView.adapter as EvolutionLineRecyclerAdapter
        val forceChange = adapter.isEditing != isEditing || adapter.isCollectionEnabled != isCollectionEnabled
        if (forceChange) {
            adapter.evolution = evolutionChain
            adapter.isEditing = isEditing
            adapter.isCollectionEnabled = isCollectionEnabled
            adapter.notifyDataSetChanged()
        } else {
            adapter.setEvolutionChain(evolutionChain)
        }
    }

    companion object {

        fun create(
            inflater: LayoutInflater,
            parent: ViewGroup?,
            spanCount: Int,
            editCardIntentions: EditCardIntentions,
            pokemonCardClicks: Relay<PokemonCardView>
        ): EvolutionChainViewHolder {
            return EvolutionChainViewHolder(inflater.inflate(R.layout.item_evolution_chain, parent, false),
                spanCount, editCardIntentions, pokemonCardClicks)
        }
    }
}
