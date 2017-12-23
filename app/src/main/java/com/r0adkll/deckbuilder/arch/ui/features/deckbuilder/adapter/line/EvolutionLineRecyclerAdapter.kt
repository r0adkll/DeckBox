package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.line


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.EditCardIntentions
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.PokemonCardViewHolder
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView
import timber.log.Timber


/**
 * A [RecyclerView.Adapter] for horizontally displaying an [EvolutionChain]
 */
class EvolutionLineRecyclerAdapter(
        val context: Context,
        val editCardIntentions: EditCardIntentions
) : RecyclerView.Adapter<PokemonCardViewHolder>(), EvolutionLineAdapter {

    private val linkSpacing: Int = context.dipToPx(24f)
    private val stageSpacing: Int = context.dipToPx(16f)

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    var evolution: EvolutionChain? = null
    var cardViewClickListener: OnPokemonCardViewClickListener? = null
    var isEditing: Boolean = false

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
        val width = (parentWidth - (2 * stageSpacing + 2 * linkSpacing)) / 3

        val lp = vh.itemView.layoutParams
        lp.width = width
        vh.itemView.layoutParams = lp

        val cardLp = vh.cardView.layoutParams as ViewGroup.MarginLayoutParams
        cardLp.marginStart = 0
        cardLp.marginEnd = 0
        vh.cardView.layoutParams = cardLp

        return vh
    }


    override fun onBindViewHolder(holder: PokemonCardViewHolder, position: Int) {
        getItem(position)?.let { card ->
            val evolution = getEvolutionState(position)
            holder.bind(card.card, card.count, evolution.evolution, isEditing)

            // Bind click listener
            holder.itemView.setOnClickListener {
                cardViewClickListener?.onClick(holder.cardView)
            }
        }
    }


    override fun getItemCount(): Int {
        return evolution?.size ?: 0
    }


    override fun getItemId(position: Int): Long {
        return getItem(position)?.card?.hashCode()?.toLong() ?: RecyclerView.NO_ID
    }


    override fun getEvolutionState(position: Int): EvolutionLineAdapter.State {
        return if (evolution != null) {
            var currentIndex = 0
            var nodeIndex = -1
            var cardIndex = -1

            evolution!!.nodes.forEachIndexed { index, node ->
                val i = position - currentIndex
                if (i < node.cards.size) {
                    nodeIndex = index
                    cardIndex = i

                    Timber.i("$this::getEvolutionState(position: $position, nodeIndex: $nodeIndex, cardIndex: $cardIndex)")
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


    fun setOnPokemonCardViewClickListener(listener: (PokemonCardView) -> Unit) {
        cardViewClickListener = object : OnPokemonCardViewClickListener {
            override fun onClick(view: PokemonCardView) {
                listener.invoke(view)
            }
        }
    }


    private fun getItem(position: Int): StackedPokemonCard? {
        return evolution?.nodes?.flatMap { it.cards }?.getOrNull(position)
    }


    private fun getEvolutionState(chain: EvolutionChain, nodeIndex: Int, cardIndex: Int): PokemonCardView.Evolution {
        val node = chain.nodes[nodeIndex]
        val isFirstNode = nodeIndex == 0
        val isFirstCard = cardIndex == 0
        val isLastCard = cardIndex == node.cards.size - 1
        val hasNextNode = nodeIndex < chain.nodes.size - 1
        if (isFirstNode) {
            if (hasNextNode && isLastCard) {
                return PokemonCardView.Evolution.END
            }
        }
        else {
            if (isFirstCard && isLastCard && hasNextNode) {
                return PokemonCardView.Evolution.MIDDLE
            }
            else if (isFirstCard) {
                return PokemonCardView.Evolution.START
            }
            else if (isLastCard && hasNextNode) {
                return PokemonCardView.Evolution.END
            }
        }

        return PokemonCardView.Evolution.NONE
    }


    interface OnPokemonCardViewClickListener {

        fun onClick(view: PokemonCardView)
    }
}