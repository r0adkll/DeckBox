package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard


/**
 * Class that collects and defines evolution chains for pokemon in a deck
 */
data class EvolutionChain(val nodes: ArrayList<Node> = ArrayList(3)) {

    val id: String
        get() = nodes.find { it.name != null }?.name ?: nodes.hashCode().toString()


    fun contains(card: PokemonCard): Boolean {
        return nodes.find { it.cards.contains(card) } != null
    }


    fun isChainFor(card: PokemonCard): Boolean {
        return nodes.find { it.name == card.name
                || it.evolvesFrom == card.name
                || (it.name != null && card.evolvesFrom != null && it.name == card.evolvesFrom)
                || (it.cards.find { it.name == card.evolvesFrom } != null) } != null
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
                // Attempt to find node for that evolution stage
                val evolvesNode = nodes.find { it.evolvesFrom == card.evolvesFrom }
                if (evolvesNode != null) {
                    // Found previous evolution form, create linking node
                    evolvesNode.cards.add(card)
                    return true
                }

                // Attempt to find node that this card evolves from
                else {
                    val evolvesFromNode = nodes.find { it.name == card.evolvesFrom }
                    if (evolvesFromNode != null) {
                        // Found previous evolution form, create linking node
                        val newNode = Node(null, card.evolvesFrom, arrayListOf(card))
                        nodes.add(newNode)
                        rectifyNodes()
                        return true
                    }
                    else {
                        // Last ditch, attempt to find evolvesFrom name in the list of cards
                        val evolvesFromNode2 = nodes.find { it.cards.find { it.name == card.evolvesFrom } != null }
                        if (evolvesFromNode2 != null) {
                            // Found previous evolution form, create linking node
                            val newNode = Node(null, card.evolvesFrom, arrayListOf(card))
                            nodes.add(newNode)
                            rectifyNodes()
                            return true
                        }
                    }
                }
            }
        }
        return false
    }


    fun removeCard(card: PokemonCard): Boolean {
        val node = nodes.find { it.cards.contains(card) }
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
            val name: String?,
            val evolvesFrom: String?,
            val cards: ArrayList<PokemonCard>
    )


    companion object {

        fun create(card: PokemonCard): EvolutionChain {
            val chain = EvolutionChain()
            // We don't want to set the name of a node unless it is the base to account for split evolutions i.e. eevee -> espeon, umbreon, etc.
            val name = if (card.evolvesFrom == null) card.name else null
            chain.nodes.add(Node(name, card.evolvesFrom, arrayListOf(card)))
            return chain
        }


        fun build(cards: List<PokemonCard>): List<EvolutionChain> {
            val chains = ArrayList<EvolutionChain>()

            cards.forEach { card ->
                val chain = chains.find { it.isChainFor(card) }
                if (chain == null) {
                    val newChain = EvolutionChain.create(card)
                    chains += newChain
                }
                else {
                    chain.addCard(card)
                }

            }

            return chains
        }
    }
}