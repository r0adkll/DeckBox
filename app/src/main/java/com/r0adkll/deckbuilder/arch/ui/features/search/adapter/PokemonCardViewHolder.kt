package com.r0adkll.deckbuilder.arch.ui.features.search.adapter


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.util.bindView


class PokemonCardViewHolder(
        itemView: View,
        val displayWhenOne: Boolean = false,
        val startDragImmediately: Boolean = false
): RecyclerView.ViewHolder(itemView) {

    private val cardView: PokemonCardView by bindView(R.id.card)


    fun bind(card: PokemonCard, count: Int) {
        cardView.displayCountWhenOne = displayWhenOne
        cardView.card = card
        cardView.count = count
        cardView.startDragImmediately = startDragImmediately

        if (displayWhenOne && count > 0) {
            cardView.elevation = dpToPx(8f)
        }
        else {
            cardView.elevation = dpToPx(4f)
        }
    }


    companion object {
        fun create(inflater: LayoutInflater,
                   parent: ViewGroup,
                   displayWhenOne: Boolean = false,
                   startDragImmediately: Boolean = false): PokemonCardViewHolder {
            val view = inflater.inflate(R.layout.item_pokemon_card, parent, false)
            return PokemonCardViewHolder(view, displayWhenOne, startDragImmediately)
        }

        fun createHorizontal(inflater: LayoutInflater,
                             parent: ViewGroup,
                             displayWhenOne: Boolean = false,
                             startDragImmediately: Boolean = false): PokemonCardViewHolder {
            val view = inflater.inflate(R.layout.item_pokemon_card_horizontal, parent, false)
            return PokemonCardViewHolder(view, displayWhenOne, startDragImmediately)
        }
    }
}