package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ftinc.kit.recycler.EmptyViewListAdapter
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.StackedPokemonCardItemCallback
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.PokemonCardViewHolder
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.util.extensions.notifyingField

class StackedPokemonRecyclerAdapter(
    context: Context,
    val addCardClicks: Relay<List<PokemonCard>> = PublishRelay.create(),
    val removeCardClicks: Relay<PokemonCard> = PublishRelay.create(),
    var itemViewClickListener: (View, StackedPokemonCard) -> Unit = { _, _ -> }
) : EmptyViewListAdapter<StackedPokemonCard, PokemonCardViewHolder>(StackedPokemonCardItemCallback()) {

    private val inflater = LayoutInflater.from(context)

    var isEditing: Boolean by notifyingField(false)
    var isCollectionEnabled: Boolean by notifyingField(false)

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonCardViewHolder {
        return PokemonCardViewHolder.create(inflater, parent, false,
            addCardClicks = addCardClicks, removeCardClicks = removeCardClicks)
    }

    override fun onBindViewHolder(vh: PokemonCardViewHolder, i: Int) {
        val card = getItem(i)
        vh.bind(
            card,
            isEditMode = isEditing,
            isCollectionMode = isCollectionEnabled,
            collectionCount = card.collection ?: 0
        )
        vh.itemView.setOnLongClickListener { v ->
            val c = v.findViewById<PokemonCardView>(R.id.card)
            c.startDrag(true)
            true
        }
        vh.itemView.setOnClickListener {
            itemViewClickListener(it, card)
        }
    }

    override fun getItemId(position: Int): Long {
        if (position != RecyclerView.NO_POSITION) {
            val item = getItem(position)
            return item.card.hashCode().toLong()
        }
        return super.getItemId(position)
    }
}
