package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.ui.components.EditCardIntentions
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.line.EvolutionLineItemDecoration
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.line.EvolutionLineRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.util.bindView


class EvolutionChainViewHolder(
        itemView: View,
        private val spanCount: Int,
        private val editCardIntentions: EditCardIntentions,
        private val pokemonCardClicks: Relay<PokemonCardView>
): androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    private val recyclerView: androidx.recyclerview.widget.RecyclerView by bindView(R.id.recycler)


    fun bind(evolutionChain: EvolutionChain, isEditing: Boolean) {

        if (recyclerView.adapter == null) {
            val adapter = EvolutionLineRecyclerAdapter(itemView.context, editCardIntentions, spanCount)
            adapter.setOnPokemonCardViewClickListener { pokemonCardClicks.accept(it) }
            recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(itemView.context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = adapter
            (recyclerView.itemAnimator as androidx.recyclerview.widget.SimpleItemAnimator).supportsChangeAnimations = false
            (0 until recyclerView.itemDecorationCount).forEach { recyclerView.removeItemDecorationAt(it) }
            recyclerView.addItemDecoration(EvolutionLineItemDecoration(itemView.context, adapter))
        }

        val adapter = recyclerView.adapter as EvolutionLineRecyclerAdapter
        val forceChange = adapter.isEditing != isEditing
        if (forceChange) {
            adapter.evolution = evolutionChain
            adapter.isEditing = isEditing
            adapter.notifyDataSetChanged()
        } else {
            adapter.setEvolutionChain(evolutionChain)
        }
    }


    companion object {

        fun create(inflater: LayoutInflater,
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