package com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.adapter


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard


/**
 * Class that collects and defines evolution chains for pokemon in a deck
 */
data class EvolutionChain(val nodes: ArrayList<Node> = ArrayList(3)) {

    val id: String
        get() = nodes.find { it.name != null }?.name ?: nodes.hashCode().toString()


    fun first(): Node? = nodes.find { it.previous == null }
    fun last(): Node? = nodes.find { it.next == null }


    fun contains(card: PokemonCard): Boolean {
        return nodes.find { it.cards.contains(card) } != null
    }


    fun isChainFor(card: PokemonCard): Boolean {
        return if (card.isBasic()) {
            nodes.find {
                it.name == card.name || it.evolvesFrom == card.name
            } != null
        }
        else {
            nodes.find {
                it.name == card.evolvesFrom || it.evolvesFrom == card.evolvesFrom
            } != null
        }
    }


    fun addCard(card: PokemonCard) {

        // if card is basic, search for basic node
        if (card.isBasic()) {
            val node = nodes.find { it.name == card.name }
            if (node != null) {
                node.cards.add(card)
            }
            else {
                // If there isn't an existing node for this card, then attempt to find the evolves from node
                val evolvedNode = nodes.find { it.evolvesFrom == card.name }
                if (evolvedNode != null) {
                    // Okay, So create teh base node linking tol this evolved nodle
                    val baseNode = Node(card.name, null, arrayListOf(card), null, evolvedNode)
                    evolvedNode.previous = baseNode
                    nodes.add(baseNode)
                }
            }
        }
        else {
            // If card is not basic, search for node with the matching evolves from
            val node = nodes.find { it.evolvesFrom == card.evolvesFrom }
            if (node != null) {
                node.cards.add(card)
            }
            else {
                // If there is no existing evolved node, then try to find the node from which this card evolves
                val evolvesFromNode = nodes.find { it.name == card.evolvesFrom }
                if (evolvesFromNode != null) {
                    val evolvedNode = Node(card.name, card.evolvesFrom, arrayListOf(card), evolvesFromNode, null)
                    evolvesFromNode.next = evolvedNode
                    nodes.add(evolvedNode)
                }
            }
        }

    }


    fun removeCard(card: PokemonCard): Boolean {
        val node = nodes.find { it.cards.contains(card) }
        if (node != null) {
            if (node.cards.remove(card) && node.cards.isEmpty()) {
                nodes.remove(node)
                return true
            }
        }
        return false
    }


    private fun PokemonCard.isBasic(): Boolean = this.evolvesFrom == null


    class Node(
            val name: String?,
            val evolvesFrom: String?,
            val cards: ArrayList<PokemonCard>,

            var previous: Node? = null,
            var next: Node? = null
    )


    companion object {

        fun create(card: PokemonCard): EvolutionChain {
            val chain = EvolutionChain()

            // We don't want to set the name of a node unless it is the base to account for split evolutions i.e. eevee -> espeon, umbreon, etc.
            val name = if (card.evolvesFrom == null) card.name else null
            val evolvesFrom = card.evolvesFrom
            val node = Node(name, evolvesFrom, arrayListOf(card))
            chain.nodes.add(node)

            return chain
        }


        fun build(cards: List<PokemonCard>): List<EvolutionChain> {
            val chains = ArrayList<EvolutionChain>()

            val sortedCards = cards.sortedBy { pokemonCard -> pokemonCard.nationalPokedexNumber }
            sortedCards.forEach { card ->
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