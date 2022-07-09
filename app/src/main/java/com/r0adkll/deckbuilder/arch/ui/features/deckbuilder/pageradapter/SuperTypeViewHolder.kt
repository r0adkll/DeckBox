package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.pageradapter

import android.content.res.Configuration
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.ftinc.kit.extensions.dp
import com.ftinc.kit.recycler.EmptyViewListAdapter
import com.ftinc.kit.widget.EmptyView
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.EditCardIntentions
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.PokemonBuilderRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.PokemonItem
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.StackedPokemonRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.util.ScreenUtils
import kotlin.math.min

/**
 * A ViewHolder interface for each [io.pokemontcg.model.SuperType] in the main deckbuilding interface
 * that dictates how that list of [io.pokemontcg.model.SuperType]'s cards are displayed
 */
abstract class SuperTypeViewHolder<out A : EmptyViewListAdapter<*, *>>(
    val itemView: View,
    @DrawableRes val emptyIcon: Int,
    @StringRes val emptyMessage: Int,
    val pokemonCardClicks: Relay<PokemonCardView>,
    val editCardIntentions: EditCardIntentions
) {

    protected val spanSize: Int
        get() {
            return if (ScreenUtils.orientation(itemView.resources, Configuration.ORIENTATION_PORTRAIT) ||
                ScreenUtils.smallestWidth(itemView.resources, ScreenUtils.Config.TABLET_10)) {
                TABLET_SPAN_SIZE
            } else {
                PHONE_SPAN_SIZE
            }
        }

    protected val recycler: androidx.recyclerview.widget.RecyclerView = itemView.findViewById(R.id.recycler)
    private val emptyView: EmptyView = itemView.findViewById(R.id.empty_view)

    abstract val adapter: A
    abstract val layoutManager: LayoutManager
    abstract fun bind(cards: List<StackedPokemonCard>)
    abstract fun wiggleCard(card: PokemonCard)
    abstract fun setEditMode(isEditing: Boolean)
    abstract fun setCollectionMode(isCollectionEnabled: Boolean)

    open fun setup() {
        emptyView.setIconResource(emptyIcon)
        emptyView.setMessage(emptyMessage)

        adapter.emptyView = emptyView
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter
        (recycler.itemAnimator as androidx.recyclerview.widget.SimpleItemAnimator).supportsChangeAnimations = false
    }

    companion object {
        private const val TABLET_SPAN_SIZE = 3
        private const val PHONE_SPAN_SIZE = 5
    }
}

/**
 * The [SuperTypeViewHolder] implementation for Pokémon cards in the deck building interface
 */
class PokemonViewHolder(
    itemView: View,
    emptyIcon: Int,
    emptyMessage: Int,
    pokemonCardClicks: Relay<PokemonCardView>,
    editCardIntentions: EditCardIntentions
) : SuperTypeViewHolder<PokemonBuilderRecyclerAdapter>(
    itemView,
    emptyIcon,
    emptyMessage,
    pokemonCardClicks,
    editCardIntentions
) {

    class PokemonSpanSizeLookup(private val lookup: (Int) -> Int) : GridLayoutManager.SpanSizeLookup() {

        override fun getSpanSize(position: Int): Int {
            return lookup.invoke(position)
        }
    }

    override val adapter: PokemonBuilderRecyclerAdapter = PokemonBuilderRecyclerAdapter(
        itemView.context,
        spanSize,
        editCardIntentions,
        pokemonCardClicks
    )

    override val layoutManager: LayoutManager = GridLayoutManager(itemView.context, spanSize)

    override fun setup() {
        super.setup()
        (layoutManager as GridLayoutManager).apply {
            spanCount = spanSize
            spanSizeLookup = PokemonSpanSizeLookup {
                val item = adapter.currentList[it]
                when (item) {
                    is PokemonItem.Evolution -> {
                        min(layoutManager.spanCount, spanSize)
                    }
                    else -> 1
                }
            }
        }
    }

    override fun bind(cards: List<StackedPokemonCard>) {
        val evolutions = EvolutionChain.build(cards)
            .sortedByDescending { chain -> chain.nodes.size }

        val items = evolutions.flatMap { chain ->
            if (chain.nodes.size > 1) {
                listOf(PokemonItem.Evolution(chain))
            } else {
                chain.nodes.flatMap { node ->
                    node.cards.map {
                        PokemonItem.Single(it)
                    }
                }
            }
        }

        adapter.submitList(items)
    }

    override fun wiggleCard(card: PokemonCard) {
    }

    override fun setEditMode(isEditing: Boolean) {
        adapter.isEditing = isEditing
    }

    override fun setCollectionMode(isCollectionEnabled: Boolean) {
        adapter.isCollectionEnabled = isCollectionEnabled
    }
}

/**
 * The [SuperTypeViewHolder] implementation for both Energy and Supporter cards in the deckbuilding
 * interface
 */
@Suppress("MagicNumber")
class TrainerEnergyViewHolder(
    itemView: View,
    emptyIcon: Int,
    emptyMessage: Int,
    pokemonCardClicks: Relay<PokemonCardView>,
    editCardIntentions: EditCardIntentions
) : SuperTypeViewHolder<StackedPokemonRecyclerAdapter>(
    itemView,
    emptyIcon,
    emptyMessage,
    pokemonCardClicks,
    editCardIntentions
) {

    override val adapter: StackedPokemonRecyclerAdapter = StackedPokemonRecyclerAdapter(itemView.context,
        editCardIntentions.addCardClicks, editCardIntentions.removeCardClicks)
    override val layoutManager: LayoutManager = GridLayoutManager(itemView.context, spanSize)

    init {
        recycler.setHasFixedSize(true)
    }

    override fun setup() {
        super.setup()
        (layoutManager as GridLayoutManager).spanCount = spanSize
    }

    override fun bind(cards: List<StackedPokemonCard>) {
        val sorted = cards.sortedBy { c ->
            when (c.card.subtype.uppercase()) {
                "SUPPORTER" -> 0
                "ITEM" -> 1
                "POKEMON TOOL" -> 2
                "POKEMON TOOL F" -> 2
                "STADIUM" -> 3
                "SPECIAL" -> 4
                else -> 10
            }
        }
        adapter.submitList(sorted)
        adapter.itemViewClickListener = { view, _ ->
            val card = view.findViewById<PokemonCardView>(R.id.card)
            pokemonCardClicks.accept(card)
        }
    }

    override fun wiggleCard(card: PokemonCard) {
        val adapterPosition = adapter.currentList.indexOfFirst { it.card.id == card.id }
        if (adapterPosition != androidx.recyclerview.widget.RecyclerView.NO_POSITION) {
            val layoutManager = recycler.layoutManager as GridLayoutManager
            val childIndex = adapterPosition - layoutManager.findFirstVisibleItemPosition()
            val child = layoutManager.getChildAt(childIndex)
            child?.let {
                val rotateAnim = RotateAnimation(-5f, 5f, Animation.RELATIVE_TO_SELF, .5f,
                    Animation.RELATIVE_TO_SELF, .5f)
                rotateAnim.repeatCount = 3
                rotateAnim.repeatMode = Animation.REVERSE
                rotateAnim.duration = 50

                val transAnim = TranslateAnimation(0f, 0f, 0f, -it.dp(8))
                transAnim.repeatCount = 1
                transAnim.repeatMode = Animation.REVERSE
                transAnim.duration = 100

                val set = AnimationSet(true)
                set.addAnimation(rotateAnim)
                set.addAnimation(transAnim)
                set.interpolator = AccelerateDecelerateInterpolator()

                it.startAnimation(set)
            }
        }
    }

    override fun setEditMode(isEditing: Boolean) {
        adapter.isEditing = isEditing
    }

    override fun setCollectionMode(isCollectionEnabled: Boolean) {
        adapter.isCollectionEnabled = isCollectionEnabled
    }
}
