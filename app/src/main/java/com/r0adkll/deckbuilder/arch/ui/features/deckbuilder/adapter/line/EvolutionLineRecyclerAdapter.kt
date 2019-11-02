@file:Suppress("MagicNumber")

package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.line

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ftinc.kit.extensions.dip
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.ui.components.EditCardIntentions
import com.r0adkll.deckbuilder.arch.ui.components.RecyclerViewBinding
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.PokemonCardViewHolder
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import com.r0adkll.deckbuilder.util.extensions.layoutWidth
import com.r0adkll.deckbuilder.util.extensions.marginsRelative

/**
 * A [RecyclerView.Adapter] for horizontally displaying an [EvolutionChain]
 */
class EvolutionLineRecyclerAdapter(
    val context: Context,
    val editCardIntentions: EditCardIntentions,
    val spanCount: Int
) : RecyclerView.Adapter<PokemonCardViewHolder>(), EvolutionLineAdapter {

    private val linkSpacing: Int = context.dip(24f)
    private val stageSpacing: Int = context.dip(16f)

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    var evolution: EvolutionChain? = null
    var cardViewClickListener: OnPokemonCardViewClickListener? = null
    var isEditing: Boolean = false
    var isCollectionEnabled: Boolean = false

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonCardViewHolder {
        val vh = PokemonCardViewHolder.create(inflater, parent, false, false,
            editCardIntentions.removeCardClicks, editCardIntentions.addCardClicks)

        var parentWidth = parent.resources.getDimensionPixelSize(R.dimen.deck_building_width)
        if (parentWidth <= 0) {
            parentWidth = parent.resources.displayMetrics.widthPixels
        }
        val width = (parentWidth - (2 * stageSpacing + (spanCount - 1) * linkSpacing)) / spanCount

        vh.itemView.layoutWidth(width)
        vh.cardView.marginsRelative(start = 0, end = 0)
        vh.collectionCounter?.marginsRelative(start = 0, end = 0)

        return vh
    }

    override fun onBindViewHolder(holder: PokemonCardViewHolder, position: Int) {
        evolution.getItem(position)?.let { card ->
            val evolution = getEvolutionState(position)
            holder.bind(
                card.card,
                card.count,
                evolution.evolution,
                isEditing,
                card.collection ?: 0,
                isCollectionEnabled
            )

            holder.itemView.setOnClickListener {
                cardViewClickListener?.onClick(holder.cardView)
            }

            holder.itemView.setOnLongClickListener { v ->
                val c = v.findViewById<PokemonCardView>(R.id.card)
                c.startDrag(true)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return evolution?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        return evolution.getItem(position)?.card?.hashCode()?.toLong() ?: RecyclerView.NO_ID
    }

    override fun getEvolutionState(position: Int): EvolutionLineAdapter.State {
        return if (evolution != null) {
            var currentIndex = 0
            var nodeIndex: Int
            var cardIndex: Int

            evolution!!.nodes.forEachIndexed { index, node ->
                val i = position - currentIndex
                if (i < node.cards.size) {
                    nodeIndex = index
                    cardIndex = i

                    val state = getEvolutionState(evolution!!, nodeIndex, cardIndex)
                    return EvolutionLineAdapter.State(nodeIndex, cardIndex, state)
                } else {
                    currentIndex += node.cards.size
                }
            }

            EvolutionLineAdapter.State(0, 0, PokemonCardView.Evolution.NONE)
        } else {
            EvolutionLineAdapter.State(0, 0, PokemonCardView.Evolution.NONE)
        }
    }

    fun setEvolutionChain(chain: EvolutionChain) {
        val diff = calculateDiff(evolution, chain)
        evolution = diff.new.first()
        diff.diff.dispatchUpdatesTo(this)
    }

    fun setOnPokemonCardViewClickListener(listener: (PokemonCardView) -> Unit) {
        cardViewClickListener = object : OnPokemonCardViewClickListener {
            override fun onClick(view: PokemonCardView) {
                listener.invoke(view)
            }
        }
    }

    private fun getEvolutionState(chain: EvolutionChain, nodeIndex: Int, cardIndex: Int): PokemonCardView.Evolution {
        val node = chain.nodes[nodeIndex]
        val isFirstNode = nodeIndex == 0
        val isFirstCard = cardIndex == 0
        val isLastCard = cardIndex == node.cards.size - 1
        val hasNextNode = nodeIndex < chain.nodes.size - 1
        return if (isFirstNode) {
            if (hasNextNode && isLastCard) {
                PokemonCardView.Evolution.END
            } else {
                PokemonCardView.Evolution.NONE
            }
        } else {
            if (isFirstCard && isLastCard && hasNextNode) {
                PokemonCardView.Evolution.MIDDLE
            } else if (isFirstCard) {
                PokemonCardView.Evolution.START
            } else if (isLastCard && hasNextNode) {
                PokemonCardView.Evolution.END
            } else {
                PokemonCardView.Evolution.NONE
            }
        }
    }

    interface OnPokemonCardViewClickListener {

        fun onClick(view: PokemonCardView)
    }

    companion object {

        fun EvolutionChain?.getItem(position: Int): StackedPokemonCard? {
            return this?.nodes?.flatMap { it.cards }?.getOrNull(position)
        }

        fun calculateDiff(old: EvolutionChain?, new: EvolutionChain): RecyclerViewBinding<EvolutionChain> {
            val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return old?.let { oldChain ->
                        val oldItem = oldChain.getItem(oldItemPosition)!!
                        val newItem = new.getItem(newItemPosition)!!
                        oldItem.card.id == newItem.card.id
                    } ?: false
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return old?.let { oldChain ->
                        val oldItem = oldChain.getItem(oldItemPosition)!!
                        val newItem = new.getItem(newItemPosition)!!
                        oldItem == newItem &&
                            oldItem.count == newItem.count &&
                            oldItem.collection == newItem.collection
                    } ?: false
                }

                override fun getOldListSize(): Int = old?.size ?: 0
                override fun getNewListSize(): Int = new.size
            })

            return RecyclerViewBinding(new = listOf(new), diff = diff)
        }
    }
}
