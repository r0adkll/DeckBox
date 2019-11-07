package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ftinc.kit.recycler.EmptyViewListAdapter
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.components.EditCardIntentions
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerViewItemCallback
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.PokemonCardViewHolder
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.util.extensions.notifyingField

class PokemonBuilderRecyclerAdapter(
    context: Context,
    private val spanCount: Int,
    private val editCardIntentions: EditCardIntentions,
    private val pokemonCardClicks: Relay<PokemonCardView>
) : EmptyViewListAdapter<PokemonItem, RecyclerView.ViewHolder>(RecyclerViewItemCallback()) {

    private val inflater = LayoutInflater.from(context)

    var isEditing: Boolean by notifyingField(false)
    var isCollectionEnabled: Boolean by notifyingField(false)

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_evolution_chain -> {
                EvolutionChainViewHolder.create(inflater, parent, spanCount, editCardIntentions, pokemonCardClicks)
            }
            else -> {
                PokemonCardViewHolder.create(inflater, parent, false,
                    addCardClicks = editCardIntentions.addCardClicks,
                    removeCardClicks = editCardIntentions.removeCardClicks)
            }
        }
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, i: Int) {
        val item = getItem(i)
        when (vh) {
            is EvolutionChainViewHolder -> {
                val evolutionChain = item as PokemonItem.Evolution
                vh.bind(evolutionChain.evolutionChain, isEditing, isCollectionEnabled)
            }
            is PokemonCardViewHolder -> {
                val single = (item as PokemonItem.Single).card
                vh.bind(
                    single,
                    isEditMode = isEditing,
                    collectionCount = single.collection ?: 0,
                    isCollectionMode = isCollectionEnabled
                )
                vh.itemView.setOnClickListener {
                    val card = it.findViewById<PokemonCardView>(R.id.card)
                    pokemonCardClicks.accept(card)
                }
                vh.itemView.setOnLongClickListener { v ->
                    val c = v.findViewById<PokemonCardView>(R.id.card)
                    c.startDrag(true)
                    true
                }
            }
        }
    }

    override fun getItemId(position: Int): Long {
        if (position != RecyclerView.NO_POSITION) {
            val item = getItem(position)
            return when (item) {
                is PokemonItem.Evolution -> item.evolutionChain.hashCode().toLong()
                is PokemonItem.Single -> item.card.card.hashCode().toLong()
            }
        }
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }
}
