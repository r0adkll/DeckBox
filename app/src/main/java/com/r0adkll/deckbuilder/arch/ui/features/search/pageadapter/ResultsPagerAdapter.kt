package com.r0adkll.deckbuilder.arch.ui.features.search.pageadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.AnimationSet
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.ftinc.kit.extensions.dp
import com.ftinc.kit.widget.EmptyView
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.EditCardIntentions
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.SearchResultsRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import io.pokemontcg.model.SuperType

@Suppress("NON_EXHAUSTIVE_WHEN")
class ResultsPagerAdapter(
    val context: Context,
    val hasValidSession: Boolean,
    private val scrollHideListener: KeyboardScrollHideListener,
    private val pokemonCardLongClicks: Relay<PokemonCardView>,
    private val editCardIntentions: EditCardIntentions
) : PagerAdapter() {

    private val inflater = LayoutInflater.from(context)
    private val viewHolders: Array<SearchResultViewHolder?> = Array(3) { null }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = inflater.inflate(R.layout.layout_deck_supertype, container, false)
        val vh = SearchResultViewHolder(view, position, hasValidSession, scrollHideListener,
            pokemonCardLongClicks, editCardIntentions)
        view.tag = vh
        viewHolders[position] = vh

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> context.getString(R.string.tab_pokemon)
        1 -> context.getString(R.string.tab_trainer)
        2 -> context.getString(R.string.tab_energy)
        else -> ""
    }

    fun setSelectedCards(cards: List<PokemonCard>) {
        viewHolders[0]?.setSelectedCards(cards)
        viewHolders[1]?.setSelectedCards(cards)
        viewHolders[2]?.setSelectedCards(cards)
    }

    fun setCards(type: SuperType, cards: List<PokemonCard>) {
        when (type) {
            SuperType.POKEMON -> viewHolders[0]?.bind(cards)
            SuperType.TRAINER -> viewHolders[1]?.bind(cards)
            SuperType.ENERGY -> viewHolders[2]?.bind(cards)
        }
    }

    fun showLoading(type: SuperType, isLoading: Boolean) {
        when (type) {
            SuperType.POKEMON -> viewHolders[0]?.showLoading(isLoading)
            SuperType.TRAINER -> viewHolders[1]?.showLoading(isLoading)
            SuperType.ENERGY -> viewHolders[2]?.showLoading(isLoading)
        }
    }

    fun showEmptyResults(type: SuperType) {
        when (type) {
            SuperType.POKEMON -> viewHolders[0]?.showEmptyResults()
            SuperType.TRAINER -> viewHolders[1]?.showEmptyResults()
            SuperType.ENERGY -> viewHolders[2]?.showEmptyResults()
        }
    }

    fun showEmptyDefault(type: SuperType) {
        when (type) {
            SuperType.POKEMON -> viewHolders[0]?.showEmptyDefault()
            SuperType.TRAINER -> viewHolders[1]?.showEmptyDefault()
            SuperType.ENERGY -> viewHolders[2]?.showEmptyDefault()
        }
    }

    fun showError(type: SuperType, description: String) {
        when (type) {
            SuperType.POKEMON -> viewHolders[0]?.showError(description)
            SuperType.TRAINER -> viewHolders[1]?.showError(description)
            SuperType.ENERGY -> viewHolders[2]?.showError(description)
        }
    }

    fun hideError(type: SuperType) {
        when (type) {
            SuperType.POKEMON -> viewHolders[0]?.hideError()
            SuperType.TRAINER -> viewHolders[1]?.hideError()
            SuperType.ENERGY -> viewHolders[2]?.hideError()
        }
    }

    fun wiggleCard(card: PokemonCard) {
        when (card.supertype) {
            SuperType.POKEMON -> viewHolders[0]?.wiggleCard(card)
            SuperType.TRAINER -> viewHolders[1]?.wiggleCard(card)
            SuperType.ENERGY -> viewHolders[2]?.wiggleCard(card)
        }
    }

    private class SearchResultViewHolder(
        itemView: View,
        val position: Int,
        val hasValidSession: Boolean,
        scrollHideListener: KeyboardScrollHideListener,
        pokemonCardLongClicks: Relay<PokemonCardView>,
        editCardIntentions: EditCardIntentions
    ) {

        private val recycler: RecyclerView = itemView.findViewById(R.id.recycler)
        private val emptyView: EmptyView = itemView.findViewById(R.id.empty_view)
        private val adapter: SearchResultsRecyclerAdapter = SearchResultsRecyclerAdapter(itemView.context,
            editCardIntentions = editCardIntentions)

        init {
            emptyView.setIconResource(R.drawable.ic_empty_search)
            emptyView.setMessage(when (position) {
                0 -> R.string.empty_search_pokemon_message
                1 -> R.string.empty_search_trainer_message
                else -> R.string.empty_search_energy_message
            })

            adapter.emptyView = emptyView
            if (hasValidSession) {
                adapter.onItemClickListener = { _, card ->
                    editCardIntentions.addCardClicks.accept(listOf(card))
                }
                adapter.onItemLongClickListener = { view, _ ->
                    // TODO: Fix this atrocity
                    val card = view.findViewById<PokemonCardView>(R.id.card)
                    pokemonCardLongClicks.accept(card)
                    true
                }
            } else {
                adapter.onItemClickListener = { view, _ ->
                    val card = view.findViewById<PokemonCardView>(R.id.card)
                    pokemonCardLongClicks.accept(card)
                }
            }

            recycler.layoutManager = GridLayoutManager(itemView.context, 3)
            recycler.adapter = adapter
            recycler.setHasFixedSize(true)

            recycler.addOnScrollListener(scrollHideListener)
        }

        fun bind(cards: List<PokemonCard>) {
            adapter.submitList(cards)
        }

        fun setSelectedCards(cards: List<PokemonCard>) {
            adapter.setSelectedCards(cards)
        }

        fun showLoading(isLoading: Boolean) {
            emptyView.state = if (isLoading) {
                EmptyView.State.LOADING
            } else {
                EmptyView.State.EMPTY
            }
        }

        fun showEmptyResults() {
            emptyView.setMessage(when (position) {
                0 -> R.string.empty_search_results_pokemon_message
                1 -> R.string.empty_search_results_trainer_message
                else -> R.string.empty_search_results_energy_message
            })
        }

        fun showEmptyDefault() {
            emptyView.setMessage(when (position) {
                0 -> R.string.empty_search_pokemon_message
                1 -> R.string.empty_search_trainer_message
                else -> R.string.empty_search_energy_message
            })
        }

        fun showError(description: String) {
            emptyView.message = description
        }

        fun hideError() {
            showEmptyDefault()
        }

        fun wiggleCard(card: PokemonCard) {
            val adapterPosition = adapter.indexOf(card)
            if (adapterPosition != RecyclerView.NO_POSITION) {
                val layoutManager = recycler.layoutManager as GridLayoutManager
                val childIndex = adapterPosition - layoutManager.findFirstVisibleItemPosition()
                val child = layoutManager.getChildAt(childIndex)
                child?.let {
                    val rotateAnim = RotateAnimation(-5f, 5f, RELATIVE_TO_SELF, .5f, RELATIVE_TO_SELF, .5f)
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
    }
}
