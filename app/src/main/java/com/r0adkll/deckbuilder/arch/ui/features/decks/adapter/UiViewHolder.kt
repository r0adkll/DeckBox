package com.r0adkll.deckbuilder.arch.ui.features.decks.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ftinc.kit.arch.util.bindView
import com.ftinc.kit.arch.util.plusAssign
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.ExpansionPreview
import com.r0adkll.deckbuilder.arch.ui.components.preview.ExpansionPreviewRenderer
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.UiViewHolder.ViewType.DECK
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.UiViewHolder.ViewType.HEADER
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.UiViewHolder.ViewType.PREVIEW
import com.r0adkll.deckbuilder.arch.ui.features.decks.adapter.UiViewHolder.ViewType.QUICK_START
import com.r0adkll.deckbuilder.arch.ui.widgets.DeckImageView
import com.r0adkll.deckbuilder.util.glide.ImageType
import com.r0adkll.deckbuilder.util.extensions.loadPokemonCard
import com.r0adkll.deckbuilder.util.stack
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

sealed class UiViewHolder<in I : Item>(itemView: View) : ViewHolder(itemView), Disposable {

    abstract fun bind(item: I)

    protected val disposables = CompositeDisposable()

    override fun isDisposed(): Boolean {
        return disposables.isDisposed
    }

    override fun dispose() {
        disposables.clear()
    }

    class PreviewViewHolder(
        itemView: View,
        private val dismissPreview: Relay<Unit>,
        private val viewPreview: Relay<ExpansionPreview>
    ) : UiViewHolder<Item.Preview>(itemView) {

        private val background by bindView<LinearLayout>(R.id.background)
        private val foreground by bindView<ImageView>(R.id.foreground)
        private val logo by bindView<ImageView>(R.id.logo)
        private val title by bindView<TextView>(R.id.title)
        private val description by bindView<TextView>(R.id.description)
        private val actionDismiss by bindView<Button>(R.id.actionDismiss)
        private val actionView by bindView<Button>(R.id.actionView)

        override fun bind(item: Item.Preview) {
            val spec = item.spec.preview

            // Load logo
            ExpansionPreviewRenderer.applyLogo(logo, spec.logo)

            // Configure Background
            disposables += ExpansionPreviewRenderer.applyBackground(background, spec.background)

            // Configure Foreground
            spec.foreground?.let { s ->
                ExpansionPreviewRenderer.applyForeground(foreground, s)?.let { disposables += it }
            }

            // Set Title & Description
            title.text = spec.title
            description.text = spec.description

            // Apply text color
            Color.parseColor(spec.textColor).apply {
                title.setTextColor(this)
                description.setTextColor(this)
                actionDismiss.setTextColor(this)
                actionView.setTextColor(this)
            }

            // Set action listeners
            actionDismiss.setOnClickListener { dismissPreview.accept(Unit) }
            actionView.setOnClickListener { viewPreview.accept(item.spec) }
        }
    }

    class QuickViewHolder(
        itemView: View,
        private val quickStart: Relay<Deck>,
        private val dismissQuickStart: Relay<Unit>
    ) : UiViewHolder<Item.QuickStart>(itemView) {

        private val recycler by bindView<RecyclerView>(R.id.recycler)
        private val actionDismiss by bindView<Button>(R.id.actionDismiss)

        override fun bind(item: Item.QuickStart) {
            // Setup recycler
            var adapter = recycler.adapter as? QuickStartRecyclerAdapter
            if (adapter == null) {
                adapter = QuickStartRecyclerAdapter(itemView.context, quickStart)
                recycler.adapter = adapter
                recycler.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            }

            val items = if (item.quickStart.templates.isNotEmpty()) {
                item.quickStart.templates.map { QuickStartRecyclerAdapter.Item.Template(it) }
            } else {
                (0 until PLACEHOLDER_COUNT).map { QuickStartRecyclerAdapter.Item.Placeholder(it) }
            }

            adapter.submitList(items)

            actionDismiss.setOnClickListener {
                dismissQuickStart.accept(Unit)
            }
        }

        companion object {
            private const val PLACEHOLDER_COUNT = 5
        }
    }

    class HeaderViewHolder(
        itemView: View
    ) : UiViewHolder<Item.Header>(itemView) {

        private val text by bindView<TextView>(R.id.title)

        override fun bind(item: Item.Header) {
            text.text = item.text
        }
    }

    class DeckViewHolder(
        itemView: View,
        private val shareClicks: Relay<Deck>,
        private val duplicateClicks: Relay<Deck>,
        private val testClicks: Relay<Deck>,
        private val deleteClicks: Relay<Deck>
    ) : UiViewHolder<Item.DeckItem>(itemView) {

        private val image by bindView<DeckImageView>(R.id.image)
        private val title by bindView<TextView>(R.id.title)
        private val loading by bindView<ProgressBar>(R.id.loading)
        private val error by bindView<ImageView>(R.id.error)
        private val actionShare by bindView<ImageView>(R.id.action_share)
        private val actionMore by bindView<ImageView>(R.id.action_more)
        private val actionTest by bindView<ImageView>(R.id.action_test)

        @SuppressLint("ClickableViewAccessibility")
        override fun bind(item: Item.DeckItem) {
            image.topCropEnabled = true

            val deck = item.validatedDeck.deck
            title.text = deck.name
            error.isVisible = deck.isMissingCards
            loading.isVisible = item.isLoading

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
                    .loadPokemonCard(itemView.context, it, ImageType.NORMAL)
                    .placeholder(R.drawable.pokemon_card_back)
                    .into(image)
            }

            val popupMenu = PopupMenu(itemView.context, actionMore)
            popupMenu.inflate(R.menu.deck_actions)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_duplicate -> {
                        duplicateClicks.accept(deck); true
                    }
                    R.id.action_delete -> {
                        deleteClicks.accept(deck); true
                    }
                    else -> false
                }
            }

            actionMore.setOnTouchListener(popupMenu.dragToOpenListener)
            actionMore.setOnClickListener {
                popupMenu.show()
            }

            actionShare.setOnClickListener { shareClicks.accept(deck) }
            actionTest.setOnClickListener { testClicks.accept(deck) }
        }

        private fun mostProminentCard(cards: List<PokemonCard>): PokemonCard? {
            val stacks = cards.stack()
            val evolutions = EvolutionChain.build(stacks)
            val largestEvolutionLine = evolutions.maxBy { it.size }
            return largestEvolutionLine?.last()?.cards?.firstOrNull()?.card
        }
    }

    private enum class ViewType(@LayoutRes val layoutId: Int) {
        PREVIEW(R.layout.item_set_preview),
        QUICK_START(R.layout.item_quickstart),
        HEADER(R.layout.item_deck_format_header),
        DECK(R.layout.item_deck);

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

        @Suppress("UNCHECKED_CAST", "LongParameterList")
        fun create(
            itemView: View,
            layoutId: Int,
            shareClicks: Relay<Deck>,
            duplicateClicks: Relay<Deck>,
            testClicks: Relay<Deck>,
            deleteClicks: Relay<Deck>,
            dismissPreview: Relay<Unit>,
            viewPreview: Relay<ExpansionPreview>,
            quickStart: Relay<Deck>,
            dismissQuickStart: Relay<Unit>
        ): UiViewHolder<Item> {
            return when (ViewType.of(layoutId)) {
                PREVIEW -> PreviewViewHolder(itemView, dismissPreview, viewPreview) as UiViewHolder<Item>
                QUICK_START -> QuickViewHolder(itemView, quickStart, dismissQuickStart) as UiViewHolder<Item>
                HEADER -> HeaderViewHolder(itemView) as UiViewHolder<Item>
                DECK -> DeckViewHolder(
                    itemView,
                    shareClicks,
                    duplicateClicks,
                    testClicks,
                    deleteClicks
                ) as UiViewHolder<Item>
            }
        }
    }
}
