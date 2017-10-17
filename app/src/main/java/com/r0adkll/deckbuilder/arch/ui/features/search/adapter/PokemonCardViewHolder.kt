package com.r0adkll.deckbuilder.arch.ui.features.search.adapter


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.util.bindView


class PokemonCardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val cardView: PokemonCardView by bindView(R.id.card)


    fun bind(card: PokemonCard) {
        cardView.card = card
    }


    companion object {
        fun create(inflater: LayoutInflater, parent: ViewGroup): PokemonCardViewHolder {
            return PokemonCardViewHolder(inflater.inflate(R.layout.item_pokemon_card, parent, false))
        }
    }
}