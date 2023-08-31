package app.deckbox.core.model

class Evolution private constructor(
  val nodes: MutableList<Node>,
) {
  private constructor(card: Stacked<Card>) : this(mutableListOf(Node(card)))

  val size: Int
    get() = nodes.size

  val count: Int
    get() = nodes.sumOf { it.cards.sumOf { it.count } }

  fun matches(card: Stacked<Card>): Boolean {
    return nodes.any { node ->
      node.name == card.card.name || // Direct node match
        node.evolvesFrom == card.card.name || // An existing node evolves from the card
        node.evolvesTo?.contains(card.card.name) == true || // An existing node evolves to the card
        (node.name == card.card.evolvesFrom) || // An existing node evolves to a card
        // An existing node evolves from the same root as the card
        (node.evolvesFrom == card.card.evolvesFrom && card.card.evolvesFrom != null) ||
        // An existing node evolves to a card that this card evolves from (missing middle child)
        node.evolvesTo?.contains(
          card.card.evolvesFrom ?: "",
        ) == true ||
        // An existing node evolves from a card that this card evolves to (missing middle child)
        card.card.evolvesTo?.contains(
          node.evolvesFrom ?: "",
        ) == true
    }
  }

  fun add(card: Stacked<Card>) {
    // First, try to find a direct node match
    val directNode = nodes.find { it.name == card.card.name }
    if (directNode != null) {
      directNode.cards += card
    } else {
      // Okay, no direct node match, but there may be a node that shares a common evolvesFrom but has a different name
      val siblingNode = nodes.find { it.evolvesFrom == card.card.evolvesFrom && card.card.evolvesFrom != null }
      if (siblingNode != null) {
        siblingNode.cards += card
      } else {
        // Attempt to insert the new node in the correct location so that the nodes align
        // 1) Find a node that this card evolves to
        // 2) Find a node that this card evolves from
        val evolvesFromNodeIndex = nodes.indexOfFirst { node ->
          node.name == card.card.evolvesFrom
        }

        if (evolvesFromNodeIndex != -1) {
          val insertIndex = (evolvesFromNodeIndex + 1).coerceAtMost(nodes.size)
          nodes.add(insertIndex, Node(card))
        } else {
          // Okay, search for node that this card evolvesTo
          val evolvesToNodeIndex = nodes.indexOfFirst { node ->
            node.evolvesFrom == card.card.name
          }

          if (evolvesToNodeIndex != -1) {
            val insertIndex = (evolvesToNodeIndex - 1).coerceAtLeast(0)
            nodes.add(insertIndex, Node(card))
          } else {
            // Lastly, we should check for missing middle evolutions
            val middleEvolvesFromIndex = nodes.indexOfFirst { node ->
              card.card.evolvesTo?.contains(node.evolvesFrom) == true
            }

            if (middleEvolvesFromIndex != -1) {
              val insertIndex = (middleEvolvesFromIndex - 1).coerceAtLeast(0)
              nodes.add(insertIndex, Node(card))
            } else {
              // Okay, no options found, just insert the node
              nodes += Node(card)
            }
          }
        }
      }
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false

    other as Evolution

    return nodes == other.nodes
  }

  override fun hashCode(): Int {
    return nodes.firstOrNull { it.evolvesFrom != null }?.evolvesFrom?.hashCode()
      ?: nodes.first().name.hashCode()
  }

  data class Node(
    val name: String,
    val evolvesFrom: String?,
    val evolvesTo: List<String>?,
    val cards: MutableList<Stacked<Card>> = mutableListOf(),
  ) {
    constructor(card: Stacked<Card>) : this(
      name = card.card.name,
      evolvesFrom = card.card.evolvesFrom,
      evolvesTo = card.card.evolvesTo,
      cards = mutableListOf(card),
    )
  }

  companion object {

    /**
     * Create a set of evolutions for a given list of card. This will build the evolution chains for pokemon cards
     * or create single node chains for individual cards
     */
    fun create(cards: List<Stacked<Card>>): List<Evolution> {
      val chains = mutableListOf<Evolution>()

      cards.forEach { card ->
        val existingChain = chains.find { it.matches(card) }
        if (existingChain != null) {
          existingChain.add(card)
        } else {
          chains += Evolution(card)
        }
      }

      return chains
    }
  }
}
