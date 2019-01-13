package com.r0adkll.deckbuilder.arch.domain.features.cards.model

import java.util.*
import kotlin.Comparator


/**
 * Class that collects and defines evolution chains for pokemon in a deck
 */
data class EvolutionChain(val nodes: ArrayList<Node> = ArrayList(3)) {

    val id: String
        get() = nodes.find { it.name != null }?.name ?: nodes.hashCode().toString()

    val size: Int
        get() = nodes.map { it.cards.size }.sum()


    fun first(): Node? = nodes.find { it.previous == null }
    fun last(): Node? = nodes.find { it.next == null }


    fun contains(card: StackedPokemonCard): Boolean {
        return nodes.find { it.cards.contains(card) } != null
    }


    fun isChainFor(card: StackedPokemonCard): Boolean {
        return if (card.isBasic()) {
            nodes.find {
                it.name == card.card.name || it.evolvesFrom == card.card.name
            } != null
        }
        else {
            nodes.find {
                it.name == card.card.evolvesFrom || it.evolvesFrom == card.card.evolvesFrom || it.evolvesFrom == card.card.name
            } != null
        }
    }


    fun addCard(card: StackedPokemonCard) {

        // if card is basic, search for basic node
        if (card.isBasic()) {
            val node = nodes.find { it.name == card.card.name }
            if (node != null) {
                node.cards.add(card)
            }
            else {
                // If there isn't an existing node for this card, then attempt to find the evolves from node
                val evolvedNode = nodes.find { it.evolvesFrom == card.card.name }
                if (evolvedNode != null) {
                    // Okay, So create teh base node linking tol this evolved nodle
                    val baseNode = Node(card.card.name, null, arrayListOf(card), null, evolvedNode)
                    evolvedNode.previous = baseNode
                    nodes.add(baseNode)
                }
            }
        }
        else {
            // If card is not basic, search for node with the matching evolves from
            val node = nodes.find { it.evolvesFrom == card.card.evolvesFrom }
            if (node != null) {
                node.cards.add(card)
            }
            else {
                // If there is no existing evolved node, then try to find the node from which this card evolves
                val evolvesFromNode = nodes.find { it.name == card.card.evolvesFrom }
                if (evolvesFromNode != null) {
                    val evolvedNode = Node(card.card.name, card.card.evolvesFrom, arrayListOf(card), evolvesFromNode, null)
                    evolvesFromNode.next = evolvedNode
                    nodes.add(evolvedNode)
                }
            }
        }

    }


    fun removeCard(card: StackedPokemonCard): Boolean {
        val node = nodes.find { it.cards.contains(card) }
        if (node != null) {
            if (node.cards.remove(card) && node.cards.isEmpty()) {
                nodes.remove(node)
                return true
            }
        }
        return false
    }


    private fun StackedPokemonCard.isBasic(): Boolean = this.card.evolvesFrom == null


    class Node(
            val name: String?,
            val evolvesFrom: String?,
            val cards: ArrayList<StackedPokemonCard>,

            var previous: Node? = null,
            var next: Node? = null
    ) {


        override fun hashCode(): Int {
            return ((this.name?.hashCode() ?: 0 * 31) +
                    (this.evolvesFrom?.hashCode() ?: 0 * 31) +
                    this.cards.hashCode() * 31)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Node

            if (name != other.name) return false
            if (evolvesFrom != other.evolvesFrom) return false
            if (!cards.equalTo(other.cards)) return false

            return true
        }

        private fun List<StackedPokemonCard>.equalTo(o: List<StackedPokemonCard>?): Boolean {
            if (o !is List<StackedPokemonCard>)
                return false

            val e1 = listIterator()
            val e2 = o.listIterator()
            while (e1.hasNext() && e2.hasNext()) {
                val o1 = e1.next()
                val o2 = e2.next()
                if (o1.card != o2.card || o1.count != o2.count)
                    return false
            }
            return !(e1.hasNext() || e2.hasNext())
        }
    }


    private class PokemonComparator : Comparator<StackedPokemonCard> {

        override fun compare(lhs: StackedPokemonCard, rhs: StackedPokemonCard): Int {
            if (lhs.card.nationalPokedexNumber == null) {
                return if (rhs.card.nationalPokedexNumber == null) 0 else -1
            } else if (rhs.card.nationalPokedexNumber == null) {
                return 1
            } else {
                val pokedexNumberResult = lhs.card.nationalPokedexNumber.compareTo(rhs.card.nationalPokedexNumber)
                return if (pokedexNumberResult == 0) {
                    lhs.card.id.compareTo(rhs.card.id)
                } else {
                    pokedexNumberResult
                }
            }
        }
    }


    companion object {

        internal fun create(card: StackedPokemonCard): EvolutionChain {
            val chain = EvolutionChain()

            // We don't want to set the name of a node unless it is the base to account for split evolutions i.e. eevee -> espeon, umbreon, etc.
            val name = card.card.name //if (card.card.evolvesFrom == null) card.card.name else null // FIXME: This makes assembly of incomplete stage 2 chains break (i.e. when you add your Stage 1 and Stage 1 and not the basic)
            val evolvesFrom = card.card.evolvesFrom
            val node = Node(name, evolvesFrom, arrayListOf(card))
            chain.nodes.add(node)

            return chain
        }


        fun build(cards: List<StackedPokemonCard>): List<EvolutionChain> {
            val chains = ArrayList<EvolutionChain>()

            val sortedCards = cards.sortedWith(PokemonComparator())
            sortedCards.forEach { card ->
                val chain = chains.find { it.isChainFor(card) }
                if (chain == null) {
                    val newChain = create(card)
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