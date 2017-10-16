package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter

import android.support.v4.view.PagerAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ftinc.kit.widget.EmptyView
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.PokemonCard
import io.pokemontcg.model.SuperType


/**
 * Pager adapter for all the [io.pokemontcg.model.SuperType]s involved in building a deck
 */
class DeckBuilderPagerAdapter(
        private val inflater: LayoutInflater
) : PagerAdapter() {

    private val pokemonCards: ArrayList<PokemonCard> = ArrayList()
    private val trainerCards: ArrayList<PokemonCard> = ArrayList()
    private val energyCards: ArrayList<PokemonCard> = ArrayList()
    private val viewHolders: Array<SuperTypeViewHolder> = emptyArray()


    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val view = inflater.inflate(R.layout.layout_deck_supertype, container, false)
        val vh = SuperTypeViewHolder(view)
        viewHolders[position] = vh
        view.tag = vh

        vh.bind(when(position) {
            0 -> pokemonCards
            1 -> trainerCards
            else -> energyCards
        })

        container?.addView(view)
        return view
    }


    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(`object` as View)
    }


    override fun isViewFromObject(view: View?, `object`: Any?): Boolean = view == `object`
    override fun getCount(): Int = 3


    fun addCard(type: SuperType, vararg card: PokemonCard) {
        addCards(type, listOf(*card))
    }


    fun addCards(type: SuperType, cards: List<PokemonCard>) {
        when(type) {
            SuperType.POKEMON -> {
                pokemonCards.addAll(cards)
                viewHolders[0].bind(pokemonCards)
            }
            SuperType.TRAINER -> {
                trainerCards.addAll(cards)
                viewHolders[1].bind(trainerCards)
            }
            SuperType.ENERGY -> {
                energyCards.addAll(cards)
                viewHolders[2].bind(energyCards)
            }
        }
    }


    fun removeCard(type: SuperType, vararg card: PokemonCard) {
        removeCards(type, listOf(*card))
    }


    fun removeCards(type: SuperType, cards: List<PokemonCard>) {
        when(type) {
            SuperType.POKEMON -> {
                pokemonCards.removeAll(cards)
                viewHolders[0].bind(pokemonCards)
            }
            SuperType.TRAINER -> {
                trainerCards.removeAll(cards)
                viewHolders[1].bind(trainerCards)
            }
            SuperType.ENERGY -> {
                energyCards.removeAll(cards)
                viewHolders[2].bind(energyCards)
            }
        }
    }


}