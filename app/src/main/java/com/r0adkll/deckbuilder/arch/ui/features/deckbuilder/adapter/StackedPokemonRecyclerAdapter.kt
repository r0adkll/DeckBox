package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter


import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.ListRecyclerAdapter
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerViewBinding
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.PokemonCardViewHolder
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.util.extensions.notifyingField


class StackedPokemonRecyclerAdapter(
        context: Context,
        val addCardClicks: Relay<List<PokemonCard>> = PublishRelay.create(),
        val removeCardClicks: Relay<PokemonCard> = PublishRelay.create()
) : ListRecyclerAdapter<StackedPokemonCard, PokemonCardViewHolder>(context) {

    var isEditing: Boolean by notifyingField(false)
    var isCollectionEnabled: Boolean by notifyingField(false)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonCardViewHolder {
        return PokemonCardViewHolder.create(inflater, parent, false,
                addCardClicks = addCardClicks, removeCardClicks = removeCardClicks)
    }


    @SuppressLint("NewApi")
    override fun onBindViewHolder(vh: PokemonCardViewHolder, i: Int) {
        super.onBindViewHolder(vh, i)
        val card = items[i]
        vh.bind(
                card.card,
                card.count,
                isEditMode = isEditing,
                isCollectionMode = isCollectionEnabled,
                collectionCount = card.collection ?: 0
        )
        vh.itemView.setOnLongClickListener { v ->
            val c = v.findViewById<PokemonCardView>(R.id.card)
            c.startDrag(true)
            true
        }
    }


    override fun getItemId(position: Int): Long {
        if (position != RecyclerView.NO_POSITION) {
            val item = items[position]
            return item.card.hashCode().toLong()
        }
        return super.getItemId(position)
    }


    fun setCards(cards: List<StackedPokemonCard>) {
        val diff = calculateDiff(items, cards)
        items = ArrayList(diff.new)
        diff.diff.dispatchUpdatesTo(DiffUpdateCallback())
    }


    companion object {
        private fun calculateDiff(old: List<StackedPokemonCard>, new: List<StackedPokemonCard>): RecyclerViewBinding<StackedPokemonCard> {
            val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = old[oldItemPosition]
                    val newItem = new[newItemPosition]
                    return oldItem.card.id == newItem.card.id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = old[oldItemPosition]
                    val newItem = new[newItemPosition]
                    return oldItem.hashCode() == newItem.hashCode()
                }

                override fun getOldListSize(): Int = old.size
                override fun getNewListSize(): Int = new.size
            })

            return RecyclerViewBinding(new = new, diff = diff)
        }
    }
}