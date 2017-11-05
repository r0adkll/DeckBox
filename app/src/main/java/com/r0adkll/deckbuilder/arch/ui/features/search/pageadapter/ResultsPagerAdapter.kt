package com.r0adkll.deckbuilder.arch.ui.features.search.pageadapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.view.animation.Animation.RELATIVE_TO_SELF
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.ftinc.kit.widget.EmptyView
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.SearchResultsRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import io.pokemontcg.model.SuperType


class ResultsPagerAdapter(
        val context: Context,
        val scrollHideListener: KeyboardScrollHideListener,
        private val pokemonCardClicks: Relay<PokemonCard>,
        private val pokemonCardLongClicks: Relay<PokemonCardView>
) : PagerAdapter() {

    private val inflater = LayoutInflater.from(context)
    private val viewHolders: Array<SearchResultViewHolder?> = Array(3, { _ -> null })


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = inflater.inflate(R.layout.layout_deck_supertype, container, false)
        val vh = SearchResultViewHolder(view, scrollHideListener, pokemonCardClicks, pokemonCardLongClicks)
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


    override fun getPageTitle(position: Int): CharSequence = when(position) {
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
        when(type) {
            SuperType.POKEMON -> viewHolders[0]?.bind(cards)
            SuperType.TRAINER -> viewHolders[1]?.bind(cards)
            SuperType.ENERGY -> viewHolders[2]?.bind(cards)
        }
    }


    fun showLoading(type: SuperType, isLoading: Boolean) {
        when(type) {
            SuperType.POKEMON -> viewHolders[0]?.showLoading(isLoading)
            SuperType.TRAINER -> viewHolders[1]?.showLoading(isLoading)
            SuperType.ENERGY -> viewHolders[2]?.showLoading(isLoading)
        }
    }


    fun showError(type: SuperType, description: String) {
        when(type) {
            SuperType.POKEMON -> viewHolders[0]?.showError(description)
            SuperType.TRAINER -> viewHolders[1]?.showError(description)
            SuperType.ENERGY -> viewHolders[2]?.showError(description)
        }
    }


    fun hideError(type: SuperType) {
        when(type) {
            SuperType.POKEMON -> viewHolders[0]?.hideError()
            SuperType.TRAINER -> viewHolders[1]?.hideError()
            SuperType.ENERGY -> viewHolders[2]?.hideError()
        }
    }


    fun wiggleCard(card: PokemonCard) {
        when(card.supertype) {
            SuperType.POKEMON -> viewHolders[0]?.wiggleCard(card)
            SuperType.TRAINER -> viewHolders[1]?.wiggleCard(card)
            SuperType.ENERGY -> viewHolders[2]?.wiggleCard(card)
        }
    }


    private class SearchResultViewHolder(
            itemView: View,
            scrollHideListener: KeyboardScrollHideListener,
            pokemonCardClicks: Relay<PokemonCard>,
            pokemonCardLongClicks: Relay<PokemonCardView>
    ) {

        private val recycler: RecyclerView = itemView.findViewById(R.id.recycler)
        private val emptyView: EmptyView = itemView.findViewById(R.id.empty_view)
        private val adapter: SearchResultsRecyclerAdapter = SearchResultsRecyclerAdapter(itemView.context)

        init {
            emptyView.setIcon(R.drawable.ic_empty_search)

            adapter.setEmptyView(emptyView)
            adapter.setOnItemClickListener { pokemonCardClicks.accept(it) }
            adapter.setOnItemLongClickListener { view, _ ->
                pokemonCardLongClicks.accept(view as PokemonCardView)
                true
            }

            recycler.layoutManager = GridLayoutManager(itemView.context, 3)
            recycler.adapter = adapter
            recycler.setHasFixedSize(true)

            recycler.addOnScrollListener(scrollHideListener)
        }


        fun bind(cards: List<PokemonCard>) {
            adapter.setCards(cards)
        }


        fun setSelectedCards(cards: List<PokemonCard>) {
            adapter.setSelectedCards(cards)
        }


        fun showLoading(isLoading: Boolean) {
            emptyView.setLoading(isLoading)
        }


        fun showError(description: String) {
            emptyView.emptyMessage = description
        }


        fun hideError() {
            emptyView.setEmptyMessage(R.string.empty_search_category)
        }


        fun wiggleCard(card: PokemonCard) {
            val adapterPosition = adapter.indexOf(card)
            if (adapterPosition != RecyclerView.NO_POSITION) {
                val child = recycler.layoutManager.getChildAt(adapterPosition)
                child?.let {
                    val rotateAnim = RotateAnimation(-5f, 5f, RELATIVE_TO_SELF, .5f, RELATIVE_TO_SELF, .5f)
                    rotateAnim.repeatCount = 3
                    rotateAnim.repeatMode = Animation.REVERSE
                    rotateAnim.duration = 50

                    val transAnim = TranslateAnimation(0f, 0f, 0f, -it.dpToPx(8f))
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