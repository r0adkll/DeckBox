package com.r0adkll.deckbuilder.arch.ui.components

import androidx.recyclerview.widget.DiffUtil
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard

class StackedPokemonCardItemCallback : DiffUtil.ItemCallback<StackedPokemonCard>() {

    override fun areItemsTheSame(oldItem: StackedPokemonCard, newItem: StackedPokemonCard): Boolean {
        return oldItem.card.id == newItem.card.id
    }

    override fun areContentsTheSame(oldItem: StackedPokemonCard, newItem: StackedPokemonCard): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}
