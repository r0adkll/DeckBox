package com.r0adkll.deckbuilder.arch.ui.features.collection.set.adapter


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


class CollectionCardViewHolder(
        itemView: View,
        val removeCardClicks: Relay<PokemonCard> = PublishRelay.create(),
        val addCardClicks: Relay<List<PokemonCard>> = PublishRelay.create()
): androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    val cardView: PokemonCardView by bindView(R.id.card)
    private val cardCount: TextView by bindView(R.id.count)
    private val actionRemove: ImageView by bindView(R.id.action_remove)
    private val actionAdd: ImageView by bindView(R.id.action_add)


    fun bind(card: PokemonCard, count: Int) {
        cardView.card = card
        if (count > 0) {
            cardCount.visible()
            cardCount.text = "$count"
        } else {
            cardCount.gone()
        }

        actionRemove.setOnClickListener { removeCardClicks.accept(card) }
        actionAdd.setOnClickListener { addCardClicks.accept(listOf(card)) }
    }


    companion object {
        fun create(inflater: LayoutInflater,
                   parent: ViewGroup,
                   removeCardClicks: Relay<PokemonCard> = PublishRelay.create(),
                   addCardClicks: Relay<List<PokemonCard>> = PublishRelay.create()): CollectionCardViewHolder {
            val view = inflater.inflate(R.layout.item_pokemon_card_collection, parent, false)
            return CollectionCardViewHolder(view,
                    removeCardClicks = removeCardClicks, addCardClicks = addCardClicks)
        }
    }
}