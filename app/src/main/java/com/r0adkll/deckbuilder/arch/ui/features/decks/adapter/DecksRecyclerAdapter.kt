package com.r0adkll.deckbuilder.arch.ui.features.decks.adapter

import android.content.Context
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.components.ListRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerViewBinding


class DecksRecyclerAdapter(
        context: Context,
        private val shareClicks: Relay<Deck>,
        private val duplicateClicks: Relay<Deck>,
        private val deleteClicks: Relay<Deck>
) : ListRecyclerAdapter<Deck, DeckViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DeckViewHolder {
        return DeckViewHolder.create(inflater, parent, shareClicks, duplicateClicks, deleteClicks)
    }


    override fun onBindViewHolder(vh: DeckViewHolder, i: Int) {
        super.onBindViewHolder(vh, i)
        vh.bind(items[i])
    }


    fun showDecks(decks: List<Deck>) {
        val diff = calculateDiff(items, decks)
        items = ArrayList(diff.new)
        diff.diff.dispatchUpdatesTo(DiffUpdateCallback())
    }


    companion object {
        fun calculateDiff(old: List<Deck>, new: List<Deck>): RecyclerViewBinding<Deck> {
            val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = old[oldItemPosition]
                    val newItem = new[newItemPosition]
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = old[oldItemPosition]
                    val newItem = new[newItemPosition]
                    return oldItem == newItem
                }

                override fun getOldListSize(): Int = old.size
                override fun getNewListSize(): Int = new.size
            })

            return RecyclerViewBinding(new = new, diff = diff)
        }
    }
}