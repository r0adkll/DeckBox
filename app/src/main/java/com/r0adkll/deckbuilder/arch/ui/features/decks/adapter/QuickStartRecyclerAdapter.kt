package com.r0adkll.deckbuilder.arch.ui.features.decks.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ftinc.kit.recycler.RecyclerViewItem
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.community.model.DeckTemplate
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerViewItemCallback
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.QuickStartRecyclerAdapter.QuickStartViewHolder.ViewType.DECK
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.QuickStartRecyclerAdapter.QuickStartViewHolder.ViewType.PLACEHOLDER
import com.r0adkll.deckbuilder.arch.ui.widgets.DeckImageView
import com.r0adkll.deckbuilder.util.CardUtils
import com.r0adkll.deckbuilder.util.bindView

class QuickStartRecyclerAdapter(
    context: Context,
    private val quickStart: Relay<Deck>
) : ListAdapter<QuickStartRecyclerAdapter.Item,
    QuickStartRecyclerAdapter.QuickStartViewHolder<QuickStartRecyclerAdapter.Item>>(RecyclerViewItemCallback()) {

    private val inflater = LayoutInflater.from(context)

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuickStartViewHolder<Item> {
        val itemView = inflater.inflate(viewType, parent, false)
        return QuickStartViewHolder.create(itemView, viewType, quickStart)
    }

    override fun onBindViewHolder(vh: QuickStartViewHolder<Item>, i: Int) {
        vh.bind(getItem(i))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).itemId
    }

    override fun onViewAttachedToWindow(holder: QuickStartViewHolder<Item>) {
        super.onViewAttachedToWindow(holder)
        holder.applyAnimation()
    }

    sealed class Item : RecyclerViewItem {

        abstract val itemId: Long

        class Placeholder(val index: Int) : Item() {

            override val layoutId: Int get() = R.layout.item_deck_placeholder
            override val itemId: Long get() = index.toLong()

            override fun isItemSame(new: RecyclerViewItem): Boolean = when (new) {
                is Placeholder -> new.index == index
                else -> false
            }

            override fun isContentSame(new: RecyclerViewItem): Boolean = false
        }

        class Template(val template: DeckTemplate) : Item() {

            override val layoutId: Int get() = R.layout.item_deck_quickstart
            override val itemId: Long get() = template.deck.id.hashCode().toLong()

            override fun isItemSame(new: RecyclerViewItem): Boolean = when (new) {
                is Template -> new.template.deck.id == template.deck.id
                else -> false
            }

            override fun isContentSame(new: RecyclerViewItem): Boolean = when (new) {
                is Template -> new.template == template
                else -> false
            }
        }
    }

    sealed class QuickStartViewHolder<in I : Item>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun bind(item: I)
        open fun applyAnimation() {}

        class PlaceholderViewHolder(itemView: View) : QuickStartViewHolder<Item.Placeholder>(itemView) {

            private val image by bindView<View>(R.id.image)
            private val title by bindView<View>(R.id.title)
            private val subtitle by bindView<View>(R.id.subtitle)

            private var set: AnimatorSet? = null

            override fun bind(item: Item.Placeholder) {
            }

            override fun applyAnimation() {
                set?.cancel()
                set = AnimatorSet().apply {
                    this.playTogether(
                        applyAnimator(image),
                        applyAnimator(title, 75L),
                        applyAnimator(subtitle, 150L)
                    )
                    this.start()
                }
            }

            private fun applyAnimator(view: View, startDelay: Long = 0L, duration: Long = 300L): AnimatorSet {
                return AnimatorSet().apply {
                    val alphaOut = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.15f)
                    alphaOut.duration = duration
                    alphaOut.interpolator = AccelerateDecelerateInterpolator()

                    val alphaIn = ObjectAnimator.ofFloat(view, "alpha", 0.15f, 1f)
                    alphaIn.duration = duration
                    alphaIn.interpolator = AccelerateDecelerateInterpolator()

                    this.startDelay = startDelay
                    this.playSequentially(alphaOut, alphaIn)
                    this.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            itemView.postDelayed({
                                animation?.start()
                            }, 1000L - (startDelay + duration))
                        }
                    })
                }
            }
        }

        class DeckViewHolder(
            itemView: View,
            private val quickStart: Relay<Deck>
        ) : QuickStartViewHolder<Item.Template>(itemView) {

            private val image by bindView<DeckImageView>(R.id.image)
            private val title by bindView<TextView>(R.id.title)
            private val subtitle by bindView<TextView>(R.id.subtitle)

            override fun bind(item: Item.Template) {
                image.topCropEnabled = true

                val deck = item.template.deck
                deck.image?.let {
                    when (it) {
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

        enum class ViewType(@LayoutRes val layoutId: Int) {
            PLACEHOLDER(R.layout.item_deck_placeholder),
            DECK(R.layout.item_deck_quickstart);

            companion object {
                val VALUES by lazy { values() }

                fun of(layoutId: Int): ViewType {
                    return VALUES.find { it.layoutId == layoutId }
                        ?: throw EnumConstantNotPresentException(
                            ViewType::class.java,
                            "could not find view type for $layoutId"
                        )
                }
            }
        }

        companion object {

            @Suppress("UNCHECKED_CAST")
            fun create(
                itemView: View,
                layoutId: Int,
                quickStart: Relay<Deck>
            ): QuickStartViewHolder<Item> {
                return when (ViewType.of(layoutId)) {
                    PLACEHOLDER -> PlaceholderViewHolder(itemView) as QuickStartViewHolder<Item>
                    DECK -> DeckViewHolder(itemView, quickStart) as QuickStartViewHolder<Item>
                }
            }
        }
    }
}
