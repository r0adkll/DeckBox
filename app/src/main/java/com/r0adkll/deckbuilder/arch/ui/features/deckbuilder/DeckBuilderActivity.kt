package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ftinc.kit.widget.EmptyView
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.di.DeckBuilderModule
import com.r0adkll.deckbuilder.internal.di.AppComponent
import io.pokemontcg.model.SuperType


class DeckBuilderActivity : BaseActivity() {

    companion object {

        fun createIntent(context: Context): Intent = Intent(context, DeckBuilderActivity::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_builder)

    }


    override fun setupComponent(component: AppComponent) {
        component.plus(DeckBuilderModule(this))
                .inject(this)
    }


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


        class SuperTypeViewHolder(itemView: View) {

            private val recycler: RecyclerView = itemView.findViewById(R.id.recycler)
            private val emptyView: EmptyView = itemView.findViewById(R.id.empty_view)

            init {
                // Setup adapter
                // Setup recycler
            }

            fun bind(cards: List<PokemonCard>) {
                // Set cards to adapter

            }
        }
    }
}