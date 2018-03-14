package com.r0adkll.deckbuilder.arch.ui.features.decks.adapter

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.UiViewHolder.ViewType.DECK
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.UiViewHolder.ViewType.PREVIEW
import com.r0adkll.deckbuilder.arch.ui.widgets.DeckImageView
import com.r0adkll.deckbuilder.util.CardUtils
import com.r0adkll.deckbuilder.util.bindView


sealed class UiViewHolder<in I : Item>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(item: I)


    class PreviewViewHolder(
            itemView: View,
            private val dismissPreview: Relay<Unit>,
            private val viewPreview: Relay<Unit>
    ) : UiViewHolder<Item.Preview>(itemView) {

        private val actionDismiss: Button by bindView(R.id.actionDismiss)
        private val actionView: Button by bindView(R.id.actionView)


        override fun bind(item: Item.Preview) {
            actionDismiss.setOnClickListener { dismissPreview.accept(Unit) }
            actionView.setOnClickListener { viewPreview.accept(Unit) }
        }
    }


    class DeckViewHolder(
            itemView: View,
            private val shareClicks: Relay<Deck>,
            private val duplicateClicks: Relay<Deck>,
            private val deleteClicks: Relay<Deck>
    ) : UiViewHolder<Item.DeckItem>(itemView) {

        private val image: DeckImageView by bindView(R.id.image)
        private val title: TextView by bindView(R.id.title)
        private val actionShare: ImageView by bindView(R.id.action_share)
        private val actionDuplicate: ImageView by bindView(R.id.action_duplicate)
        private val actionDelete: ImageView by bindView(R.id.action_delete)


        override fun bind(item: Item.DeckItem) {
            val deck = item.deck
            title.text = deck.name

            deck.image?.let {
                when(it) {
                    is DeckImage.Pokemon -> {
                        GlideApp.with(itemView)
                                .load(it.imageUrl)
                                .placeholder(R.drawable.pokemon_card_back)
                                .into(image)
                    }
                    is DeckImage.Type -> {
                        image.primaryType = it.type1
                        image.secondaryType = it.type2
                    }
                }
            } ?: mostProminentCard(deck.cards)?.let {
                GlideApp.with(itemView)
                        .load(it.imageUrl)
                        .placeholder(R.drawable.pokemon_card_back)
                        .into(image)
            }

            actionShare.setOnClickListener { shareClicks.accept(deck) }
            actionDuplicate.setOnClickListener { duplicateClicks.accept(deck) }
            actionDelete.setOnClickListener { deleteClicks.accept(deck) }
        }


        private fun mostProminentCard(cards: List<PokemonCard>): PokemonCard? {
            val stacks = CardUtils.stackCards().invoke(cards)
            val evolutions = EvolutionChain.build(stacks)
            val largestEvolutionLine = evolutions.maxBy { it.size }
            return largestEvolutionLine?.last()?.cards?.firstOrNull()?.card
        }
    }


    private enum class ViewType(@LayoutRes val layoutId: Int) {
        PREVIEW(R.layout.item_set_preview),
        DECK(R.layout.item_deck);

        companion object {
            val VALUES by lazy { values() }

            fun of(layoutId: Int): ViewType {
                val match = VALUES.firstOrNull { it.layoutId == layoutId }
                match?.let { return match }

                throw EnumConstantNotPresentException(ViewType::class.java, "could not find view type for $layoutId")
            }
        }
    }


    companion object {

        @Suppress("UNCHECKED_CAST")
        fun create(itemView: View,
                   layoutId: Int,
                   shareClicks: Relay<Deck>,
                   duplicateClicks: Relay<Deck>,
                   deleteClicks: Relay<Deck>,
                   dismissPreview: Relay<Unit>,
                   viewPreview: Relay<Unit>): UiViewHolder<Item> {
            val viewType = ViewType.of(layoutId)
            return when(viewType) {
                PREVIEW -> PreviewViewHolder(itemView, dismissPreview, viewPreview) as UiViewHolder<Item>
                DECK -> DeckViewHolder(itemView, shareClicks, duplicateClicks, deleteClicks) as UiViewHolder<Item>
            }
        }
    }
}