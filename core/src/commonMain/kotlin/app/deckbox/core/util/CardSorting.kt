package app.deckbox.core.util

import app.deckbox.core.model.Card
import app.deckbox.core.model.Evolution
import app.deckbox.core.model.Stacked
import app.deckbox.core.model.SuperType

/**
 * Sort a list of stacked pokemon cards in a way that would be consistent with how they would be sorted
 * in the deck builder. Focusing on pokemon evolutions first, by name, and then singles, then trainers and
 * energy
 */
fun List<Stacked<Card>>.sortCards(): List<Stacked<Card>> {
  val pokemon = filter { it.card.supertype == SuperType.POKEMON }
  val trainers = filter { it.card.supertype == SuperType.TRAINER }.sortedBy { it.card.name }
  val energy = filter { it.card.supertype == SuperType.ENERGY }.sortedBy { it.card.name }

  val sortedPokemon = Evolution.create(pokemon)
    .flatMap { evolution ->
      if (evolution.size == 1) {
        evolution.firstNodeCards().map {
          CardNode.Single(it)
        }
      } else {
        listOf(CardNode.EvolutionLine(evolution))
      }
    }
    .sortedBy { node ->
      when (node) {
        is CardNode.EvolutionLine -> node.evolution.firstNodeCards().first().card.name
        is CardNode.Single -> "zzzz${node.card.card.name}"
      }
    }
    .flatMap { node ->
      when (node) {
        is CardNode.EvolutionLine -> node.evolution.nodes.flatMap { it.cards }
        is CardNode.Single -> listOf(node.card)
      }
    }

  return sortedPokemon + trainers + energy
}

private sealed interface CardNode {
  class EvolutionLine(val evolution: Evolution) : CardNode
  class Single(val card: Stacked<Card>) : CardNode
}
