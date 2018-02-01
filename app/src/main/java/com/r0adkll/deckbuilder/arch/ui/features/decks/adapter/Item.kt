package com.r0adkll.deckbuilder.arch.ui.features.decks.adapter


import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerItem


sealed class Item : RecyclerItem{

    object Preview : Item() {

        override fun isItemSame(new: RecyclerItem): Boolean = new == Preview
        override fun isContentSame(new: RecyclerItem): Boolean = new == Preview

        override val layoutId: Int = R.layout.item_set_preview
    }


    data class DeckItem(val deck: Deck) : Item() {

        override fun isItemSame(new: RecyclerItem): Boolean = when(new) {
            is DeckItem -> new.deck.id == deck.id
            else -> false
        }


        override fun isContentSame(new: RecyclerItem): Boolean = when(new) {
            is DeckItem -> new.deck == deck
            else -> false
        }


        override val layoutId: Int = R.layout.item_deck
    }

}