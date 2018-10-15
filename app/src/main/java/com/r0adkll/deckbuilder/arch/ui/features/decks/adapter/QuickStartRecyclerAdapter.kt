package com.r0adkll.deckbuilder.arch.ui.features.decks.adapter

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.arch.ui.components.ListRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerItem
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.community.model.DeckTemplate
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.QuickStartRecyclerAdapter.QuickStartViewHolder.ViewType.PLACEHOLDER
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.QuickStartRecyclerAdapter.QuickStartViewHolder.ViewType.DECK
import com.r0adkll.deckbuilder.arch.ui.widgets.DeckImageView
import com.r0adkll.deckbuilder.util.CardUtils
import com.r0adkll.deckbuilder.util.bindView


class QuickStartRecyclerAdapter(
        context: Context,
        private val quickStart: Relay<Deck>
) : ListRecyclerAdapter<QuickStartRecyclerAdapter.Item,
        QuickStartRecyclerAdapter.QuickStartViewHolder<QuickStartRecyclerAdapter.Item>>(context) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuickStartViewHolder<Item> {
        val itemView = inflater.inflate(viewType, parent, false)
        return QuickStartViewHolder.create(itemView, viewType, quickStart)
    }


    override fun onBindViewHolder(vh: QuickStartViewHolder<Item>, i: Int) {
        vh.bind(items[i])
    }


    override fun getItemViewType(position: Int): Int {
        return items[position].viewType
    }


    fun setQuickStartItems(quickStartItems: List<Item>) {
        val diff = calculateDiff(quickStartItems, items)
        items = ArrayList(quickStartItems)
        diff.diff.dispatchUpdatesTo(getListUpdateCallback())
    }


    sealed class Item : RecyclerItem {

        class Placeholder(val index: Int) : Item() {

            override val layoutId: Int = R.layout.item_deck_placeholder

            override fun isItemSame(new: RecyclerItem): Boolean = when(new) {
                is Placeholder -> new.index == index
                else -> false
            }

            override fun isContentSame(new: RecyclerItem): Boolean = false
        }

        class Template(val template: DeckTemplate): Item() {

            override val layoutId: Int = R.layout.item_deck_quickstart

            override fun isItemSame(new: RecyclerItem): Boolean = when(new) {
                is Template -> new.template.deck.id == template.deck.id
                else -> false
            }

            override fun isContentSame(new: RecyclerItem): Boolean = when(new) {
                is Template -> new.template == template
                else -> false
            }
        }
    }


    sealed class QuickStartViewHolder<in I : Item>(itemView: View): RecyclerView.ViewHolder(itemView) {

        abstract fun bind(item: I)


        class PlaceholderViewHolder(itemView: View): QuickStartViewHolder<Item.Placeholder>(itemView) {

            override fun bind(item: Item.Placeholder) {
                // Do Nothing
            }
        }


        class DeckViewHolder(
                itemView: View,
                private val quickStart: Relay<Deck>
        ): QuickStartViewHolder<Item.Template>(itemView) {

            private val image by bindView<DeckImageView>(R.id.image)
            private val title by bindView<TextView>(R.id.title)
            private val subtitle by bindView<TextView>(R.id.subtitle)


            override fun bind(item: Item.Template) {
                val deck = item.template.deck
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

                title.text = item.template.name
                subtitle.text = item.template.description

                itemView.setOnClickListener {
                    quickStart.accept(deck)
                }
            }


            private fun mostProminentCard(cards: List<PokemonCard>): PokemonCard? {
                val stacks = CardUtils.stackCards().invoke(cards)
                val evolutions = EvolutionChain.build(stacks)
                val largestEvolutionLine = evolutions.maxBy { it.size }
                return largestEvolutionLine?.last()?.cards?.firstOrNull()?.card
            }
        }


        private enum class ViewType(@LayoutRes val layoutId: Int) {
            PLACEHOLDER(R.layout.item_deck_placeholder),
            DECK(R.layout.item_deck_quickstart);

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
                       quickStart: Relay<Deck>) : QuickStartViewHolder<Item> {
                val viewType = ViewType.of(layoutId)
                return when(viewType) {
                    PLACEHOLDER -> PlaceholderViewHolder(itemView) as QuickStartViewHolder<Item>
                    DECK -> DeckViewHolder(itemView, quickStart) as QuickStartViewHolder<Item>
                }
            }
        }
    }

}