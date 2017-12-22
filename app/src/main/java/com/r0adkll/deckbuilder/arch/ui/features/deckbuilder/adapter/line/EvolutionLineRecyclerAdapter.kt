package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.line


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.EditCardIntentions
import com.r0adkll.deckbuilder.arch.ui.features.search.adapter.PokemonCardViewHolder
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView


/**
 * A [RecyclerView.Adapter] for horizontally displaying an [EvolutionChain]
 */
class EvolutionLineRecyclerAdapter(
        val context: Context,
        val editCardIntentions: EditCardIntentions
) : RecyclerView.Adapter<PokemonCardViewHolder>() {

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

        return vh
    }


    override fun onBindViewHolder(holder: PokemonCardViewHolder, position: Int) {
        getItem(position)?.let { card ->
            holder.bind(card.card, card.count, isEditing)

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


    interface OnPokemonCardViewClickListener {

        fun onClick(view: PokemonCardView)
    }
}