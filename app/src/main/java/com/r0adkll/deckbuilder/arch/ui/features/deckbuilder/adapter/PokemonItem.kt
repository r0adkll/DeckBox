package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter

import com.ftinc.kit.recycler.RecyclerViewItem
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard

sealed class PokemonItem : RecyclerViewItem {

    data class Evolution(val evolutionChain: EvolutionChain) : PokemonItem() {

        override val layoutId: Int get() = R.layout.item_evolution_chain

        override fun isItemSame(new: RecyclerViewItem): Boolean = when (new) {
            is Evolution -> new.evolutionChain.id == evolutionChain.id
            else -> false
        }

        override fun isContentSame(new: RecyclerViewItem): Boolean = when (new) {
            is Evolution -> new.evolutionChain == evolutionChain
            else -> false
        }
    }

    data class Single(val card: StackedPokemonCard) : PokemonItem() {

        override val layoutId: Int get() = R.layout.item_pokemon_card_editable

        override fun isItemSame(new: RecyclerViewItem): Boolean = when (new) {
            is Single -> new.card.card.id == card.card.id
            else -> false
        }

        override fun isContentSame(new: RecyclerViewItem): Boolean = when (new) {
            is Single -> new.card.hashCode() == card.hashCode()
            else -> false
        }
    }
}
