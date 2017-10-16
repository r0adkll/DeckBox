package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter


import com.r0adkll.deckbuilder.arch.domain.PokemonCard


/**
 * Class that collects and defines evolution chains for pokemon in a deck
 */
class EvolutionChain {

    val nodes: ArrayList<Node> = ArrayList(3)


    fun contains(card: PokemonCard): Boolean {
        return nodes.find { it.name == card.name }?.cards?.find { it.id == card.id } != null
    }


    fun addCard(card: PokemonCard): Boolean {
        val node = nodes.find { it.name == card.name }
        if (node != null) {
            node.cards.add(card)
            rectifyNodes()
            return true
        }
        else {
            if (card.evolvesFrom == null) {
                // If card is a basic, 'i.e.' base of a potential evolution, search for an evolution of
                val evolution = nodes.find { it.evolvesFrom == card.name }
                if (evolution != null) {
                    val newNode = Node(card.name, card.evolvesFrom, arrayListOf(card))
                    nodes.add(newNode)
                    rectifyNodes()
                    return true
                }
            }
            else {
                // Attempt to find node for previous evolution stage
                val evolvesFromNode = nodes.find { it.name == card.evolvesFrom }
                if (evolvesFromNode != null) {
                    // Found previous evolution form, create linking node
                    val newNode = Node(card.name, card.evolvesFrom, arrayListOf(card))
                    nodes.add(newNode)
                    rectifyNodes()
                    return true
                }
            }
        }
        return false
    }


    fun removeCard(card: PokemonCard): Boolean {
        val node = nodes.find { it.name == card.name }
        if (node != null) {
            if (node.cards.remove(card) && node.cards.isEmpty()) {
                nodes.remove(node)
                rectifyNodes()
                return true
            }
        }
        return false
    }


    private fun rectifyNodes() {
        nodes.sortBy { node -> node.evolvesFrom != null }
    }


    class Node(
            val name: String,
            val evolvesFrom: String?,
            val cards: ArrayList<PokemonCard>
    )


    companion object {

        fun create(card: PokemonCard): EvolutionChain {
            val chain = EvolutionChain()
            chain.nodes.add(Node(card.name, card.evolvesFrom, arrayListOf(card)))
            return chain
        }
    }
}