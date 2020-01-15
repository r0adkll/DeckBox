package com.r0adkll.deckbuilder.arch.ui.features.decks.adapter

import com.ftinc.kit.recycler.RecyclerViewItem
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.ValidatedDeck
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.ExpansionPreview
import com.r0adkll.deckbuilder.arch.ui.features.decks.DecksUi

sealed class Item : RecyclerViewItem {

    abstract val itemId: Long

    data class Header(val text: String) : Item() {

        override val layoutId: Int get() = R.layout.item_deck_format_header
        override val itemId: Long get() = text.hashCode().toLong()

        override fun isItemSame(new: RecyclerViewItem): Boolean = when (new) {
            is Header -> new.text == text
            else -> false
        }

        override fun isContentSame(new: RecyclerViewItem): Boolean = when (new) {
            is Header -> new.text == text
            else -> false
        }
    }

    data class Preview(val spec: ExpansionPreview) : Item() {

        override val layoutId: Int get() = R.layout.item_set_preview
        override val itemId: Long get() = 1L

        override fun isItemSame(new: RecyclerViewItem): Boolean = when (new) {
            is Preview -> new.spec.version == spec.version
            else -> false
        }

        override fun isContentSame(new: RecyclerViewItem): Boolean = when (new) {
            is Preview -> new.spec == spec
            else -> false
        }
    }

    data class QuickStart(val quickStart: DecksUi.QuickStart) : Item() {

        override val layoutId: Int get() = R.layout.item_quickstart
        override val itemId: Long get() = 0L

        override fun isItemSame(new: RecyclerViewItem): Boolean = new is QuickStart

        override fun isContentSame(new: RecyclerViewItem): Boolean = when (new) {
            is QuickStart -> new.quickStart.templates == quickStart.templates
            else -> false
        }
    }

    data class DeckItem(val validatedDeck: ValidatedDeck) : Item() {

        override val layoutId: Int get() = R.layout.item_deck
        override val itemId: Long get() = validatedDeck.deck.id.hashCode().toLong()

        override fun isItemSame(new: RecyclerViewItem): Boolean = when (new) {
            is DeckItem -> new.validatedDeck.deck.id == validatedDeck.deck.id
            else -> false
        }

        override fun isContentSame(new: RecyclerViewItem): Boolean = when (new) {
            is DeckItem -> new.validatedDeck == validatedDeck
            else -> false
        }
    }
}
