package com.r0adkll.deckbuilder.arch.ui.features.decks.adapter


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.GlideApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.util.bindView


class DeckViewHolder(
        itemView: View,
        private val shareClicks: Relay<Deck>,
        private val duplicateClicks: Relay<Deck>,
        private val deleteClicks: Relay<Deck>
) : RecyclerView.ViewHolder(itemView) {

    private val image: ImageView by bindView(R.id.image)
    private val title: TextView by bindView(R.id.title)
    private val actionShare: ImageView by bindView(R.id.action_share)
    private val actionDuplicate: ImageView by bindView(R.id.action_duplicate)
    private val actionDelete: ImageView by bindView(R.id.action_delete)


    fun bind(deck: Deck) {
        title.text = deck.name
        deck.cards.firstOrNull()?.let {
            GlideApp.with(itemView)
                    .load(it.imageUrl)
                    .placeholder(R.drawable.pokemon_card_back)
                    .into(image)
        }

        actionShare.setOnClickListener { shareClicks.accept(deck) }
        actionDuplicate.setOnClickListener { duplicateClicks.accept(deck) }
        actionDelete.setOnClickListener { deleteClicks.accept(deck) }
    }


    companion object {
        fun create(inflater: LayoutInflater, parent: ViewGroup?, shareClicks: Relay<Deck>,
                   duplicateClicks: Relay<Deck>, deleteClicks: Relay<Deck>): DeckViewHolder {
            return DeckViewHolder(inflater.inflate(R.layout.item_deck, parent, false),
                    shareClicks, duplicateClicks, deleteClicks)
        }
    }
}