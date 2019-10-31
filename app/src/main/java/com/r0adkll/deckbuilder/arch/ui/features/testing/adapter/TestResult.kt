package com.r0adkll.deckbuilder.arch.ui.features.testing.adapter

import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerItem

data class TestResult(val pokemonCard: PokemonCard?, val percentage: Float, val maxPercentage: Float) : RecyclerItem {

    override val layoutId: Int = R.layout.item_test_result

    override fun isItemSame(new: RecyclerItem): Boolean = when(new) {
        is TestResult -> pokemonCard == new.pokemonCard
        else -> false
    }

    override fun isContentSame(new: RecyclerItem): Boolean = when(new) {
        is TestResult -> pokemonCard == new.pokemonCard
        else -> false
    }
}
