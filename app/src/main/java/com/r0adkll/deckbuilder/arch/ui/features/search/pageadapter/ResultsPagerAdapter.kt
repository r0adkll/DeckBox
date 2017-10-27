package com.r0adkll.deckbuilder.arch.ui.features.search.pageadapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ftinc.kit.widget.EmptyView
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.SearchResultsRecyclerAdapter
import io.pokemontcg.model.SuperType


class ResultsPagerAdapter(
        val context: Context,
        val scrollHideListener: KeyboardScrollHideListener,
        private val pokemonCardClicks: Relay<PokemonCard>
) : PagerAdapter() {

    private val inflater = LayoutInflater.from(context)
    private val viewHolders: Array<SearchResultViewHolder?> = Array(3, { _ -> null })


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = inflater.inflate(R.layout.layout_deck_supertype, container, false)
        val vh = SearchResultViewHolder(view, scrollHideListener, pokemonCardClicks)
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


    private class SearchResultViewHolder(
            itemView: View,
            scrollHideListener: KeyboardScrollHideListener,
            pokemonCardClicks: Relay<PokemonCard>
    ) {

        private val recycler: RecyclerView = itemView.findViewById(R.id.recycler)
        private val emptyView: EmptyView = itemView.findViewById(R.id.empty_view)
        private val adapter: SearchResultsRecyclerAdapter = SearchResultsRecyclerAdapter(itemView.context)

        init {
            emptyView.setIcon(R.drawable.ic_empty_search)

            adapter.setEmptyView(emptyView)
            adapter.setOnItemClickListener { pokemonCardClicks.accept(it) }

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
    }
}