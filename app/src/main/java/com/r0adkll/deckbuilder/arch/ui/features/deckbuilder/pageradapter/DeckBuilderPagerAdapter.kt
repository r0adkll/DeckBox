package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.pageradapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import io.pokemontcg.model.SuperType


/**
 * Pager adapter for all the [io.pokemontcg.model.SuperType]s involved in building a deck
 */
class DeckBuilderPagerAdapter(
        private val context: Context,
        private val pokemonCardClicks: Relay<PokemonCard>
) : PagerAdapter() {

    private val inflater = LayoutInflater.from(context)
    private val viewHolders: Array<SuperTypeViewHolder<*>?> = Array(3, { _ -> null })


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = inflater.inflate(R.layout.layout_deck_supertype, container, false)
        val vh = getViewHolder(position, view, pokemonCardClicks)
        vh.setup()
        viewHolders[position] = vh
        view.tag = vh
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

    fun setCards(type: SuperType, cards: List<StackedPokemonCard>) {
        when(type) {
            SuperType.POKEMON -> {
                viewHolders[0]?.bind(cards)
            }
            SuperType.TRAINER -> {
                viewHolders[1]?.bind(cards)
            }
            SuperType.ENERGY -> {
                viewHolders[2]?.bind(cards)
            }
        }
    }


    private fun getViewHolder(position: Int, itemView: View, pokemonCardClicks: Relay<PokemonCard>): SuperTypeViewHolder<*> {
        val emptyIcon = when(position) {
            0 -> R.drawable.ic_empty_pokeball
            1 -> R.drawable.ic_empty_wrench
            else -> R.drawable.ic_empty_flash
        }
        val emptyMessage = when(position) {
            0 -> R.string.empty_deck_pokemon_message
            1 -> R.string.empty_deck_trainer_message
            else -> R.string.empty_deck_energy_message
        }

        return when(position) {
            0 -> PokemonViewHolder(itemView, emptyIcon, emptyMessage, pokemonCardClicks)
            else -> TrainerEnergyViewHolder(itemView, emptyIcon, emptyMessage, pokemonCardClicks)
        }
    }

}