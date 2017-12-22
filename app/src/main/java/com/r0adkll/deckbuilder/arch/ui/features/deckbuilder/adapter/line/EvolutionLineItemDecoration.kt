package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter.line

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.EvolutionChain
import com.r0adkll.deckbuilder.arch.ui.widgets.PokemonCardView


class EvolutionLineItemDecoration(

) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
    }


    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
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
}