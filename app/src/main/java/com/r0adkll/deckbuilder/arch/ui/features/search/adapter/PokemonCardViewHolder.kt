package com.r0adkll.deckbuilder.arch.ui.features.search.adapter


import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ftinc.kit.kotlin.extensions.dpToPx
import com.ftinc.kit.kotlin.extensions.gone
import com.ftinc.kit.kotlin.extensions.setVisible
import com.ftinc.kit.kotlin.extensions.visible
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.util.bindOptionalView
import com.r0adkll.deckbuilder.util.bindView


class PokemonCardViewHolder(
        itemView: View,
        val displayWhenOne: Boolean = false,
        val startDragImmediately: Boolean = false,
        val removeCardClicks: Relay<PokemonCard> = PublishRelay.create(),
        val addCardClicks: Relay<List<PokemonCard>> = PublishRelay.create()
): RecyclerView.ViewHolder(itemView) {

    val cardView: PokemonCardView by bindView(R.id.card)
    private val actionLayout: LinearLayout? by bindOptionalView(R.id.action_layout)
    private val actionRemove: ImageView? by bindOptionalView(R.id.action_remove)
    private val actionAdd: ImageView? by bindOptionalView(R.id.action_add)
    val collectionCounter: TextView? by bindOptionalView(R.id.count)


    fun bind(card: PokemonCard,
             count: Int,
             evolution: PokemonCardView.Evolution = PokemonCardView.Evolution.NONE,
             isEditMode: Boolean = false,
             collectionCount: Int = 0,
             isCollectionMode: Boolean = false) {

        cardView.displayCountWhenOne = displayWhenOne
        cardView.card = card
        cardView.count = count
        cardView.startDragImmediately = startDragImmediately
        cardView.evolution = evolution

        if (isCollectionMode) {
            collectionCounter?.visible()
            collectionCounter?.text = "$collectionCount"
            cardView.alpha = if (collectionCount >= count) 1f else 0.4f
        } else {
            collectionCounter?.gone()
            cardView.alpha = 1f
        }

        actionLayout?.setVisible((isEditMode && !displayWhenOne) || isEditMode && ((displayWhenOne && count > 0) || count > 1))
        actionRemove?.setOnClickListener { removeCardClicks.accept(card) }
        actionAdd?.setOnClickListener { addCardClicks.accept(listOf(card)) }

        if (displayWhenOne && count > 0) {
            cardView.elevation = dpToPx(8f)
        }
        else {
            cardView.elevation = dpToPx(4f)
        }
    }


    companion object {
        fun create(inflater: LayoutInflater,
                   parent: ViewGroup,
                   displayWhenOne: Boolean = false,
                   startDragImmediately: Boolean = false,
                   removeCardClicks: Relay<PokemonCard> = PublishRelay.create(),
                   addCardClicks: Relay<List<PokemonCard>> = PublishRelay.create()): PokemonCardViewHolder {
            val layout = if (startDragImmediately) R.layout.item_pokemon_card else R.layout.item_pokemon_card_collection
            val view = inflater.inflate(layout, parent, false)
            return PokemonCardViewHolder(view, displayWhenOne, startDragImmediately,
                    removeCardClicks = removeCardClicks, addCardClicks = addCardClicks)
        }

        fun createHorizontal(inflater: LayoutInflater,
                             parent: ViewGroup,
                             displayWhenOne: Boolean = false,
                             startDragImmediately: Boolean = false): PokemonCardViewHolder {
            val view = inflater.inflate(R.layout.item_pokemon_card_horizontal, parent, false)
            return PokemonCardViewHolder(view, displayWhenOne, startDragImmediately)
        }
    }
}