package com.r0adkll.deckbuilder.arch.ui.features.decks.adapter

import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.ExpansionPreview
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.ValidatedDeck
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerItem
import com.r0adkll.deckbuilder.arch.ui.features.decks.DecksUi

sealed class Item : RecyclerItem{

    data class Header(val text: String) : Item() {

        override fun isItemSame(new: RecyclerItem): Boolean = when(new) {
            is Header -> new.text == text
            else -> false
        }

        override fun isContentSame(new: RecyclerItem): Boolean = when(new) {
            is Header -> new.text == text
            else -> false
        }

        override val layoutId: Int = R.layout.item_deck_format_header
    }

    data class Preview(val spec: ExpansionPreview) : Item() {

        override fun isItemSame(new: RecyclerItem): Boolean = when(new) {
            is Preview -> new.spec.version == spec.version
            else -> false
        }

        override fun isContentSame(new: RecyclerItem): Boolean = when(new) {
            is Preview -> new.spec == spec
            else -> false
        }

        override val layoutId: Int = R.layout.item_set_preview
    }

    data class QuickStart(val quickStart: DecksUi.QuickStart): Item() {

        override fun isItemSame(new: RecyclerItem): Boolean = new is QuickStart

        override fun isContentSame(new: RecyclerItem): Boolean = when(new) {
            is QuickStart -> new.quickStart.templates == quickStart.templates
            else -> false
        }

        override val layoutId: Int = R.layout.item_quickstart
    }

    data class DeckItem(val validatedDeck: ValidatedDeck, val isLoading: Boolean) : Item() {

        override fun isItemSame(new: RecyclerItem): Boolean = when(new) {
            is DeckItem -> new.validatedDeck.deck.id == validatedDeck.deck.id
            else -> false
        }

        override fun isContentSame(new: RecyclerItem): Boolean = when(new) {
            is DeckItem -> new.validatedDeck == validatedDeck && new.isLoading == isLoading
            else -> false
        }

        override val layoutId: Int = R.layout.item_deck
    }
}
