package com.r0adkll.deckbuilder.arch.ui.features.playtest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView


class HandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val cardView = itemView as PokemonCardView


    fun bind(card: PokemonCard) {

    }


    companion object {

        fun create(inflater: LayoutInflater, parent: ViewGroup?): HandViewHolder {
            return HandViewHolder(inflater.inflate(R.layout.item_pokemon_card, parent, false))
        }
    }
}