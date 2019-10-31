package com.r0adkll.deckbuilder.arch.ui.features.testing.adapter

import com.ftinc.kit.recycler.RecyclerViewItem
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard

data class TestResult(
        val pokemonCard: PokemonCard?,
        val percentage: Float,
        val maxPercentage: Float
) : RecyclerViewItem {

    override val layoutId: Int get() = R.layout.item_test_result

    override fun isItemSame(new: RecyclerViewItem): Boolean = when(new) {
        is TestResult -> pokemonCard == new.pokemonCard
        else -> false
    }

    override fun isContentSame(new: RecyclerViewItem): Boolean = when(new) {
        is TestResult -> pokemonCard == new.pokemonCard
        else -> false
    }
}
