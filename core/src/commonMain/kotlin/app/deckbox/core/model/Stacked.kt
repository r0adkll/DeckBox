package app.deckbox.core.model

/**
 * Card wrapper to add stack and collection metadata to a card
 */
data class Stacked<CardT>(
  val card: CardT,
  val count: Int = 1,
  val collected: Int? = null,
)

/**
 * Create a stack from an individual card
 * @param count the count of the stack, i.e. it's "height"
 * @param collected the collection count for this card
 */
fun <CardT> CardT.stack(
  count: Int = 1,
  collected: Int? = null,
): Stacked<CardT> = Stacked(
  card = this,
  count = count,
  collected = collected,
)

/**
 * Map a [Stacked] content to another type
 */
fun <T, R> Stacked<T>.map(mapper: (T) -> R): Stacked<R> {
  return Stacked(
    card = mapper(card),
    count = count,
    collected = collected,
  )
}

// /**
// * Stack a list of cards into a list of stacked counted cards by their name
// * @param collection Optional. The collection count in which to also provide the collection count to the stacked card
// * @return list of stacked cards
// */
// fun <CardT> List<CardT>.stackByName(collection: Collection<CardT>? = null): List<StackedCard<CardT>> {
//  return stack({ it.name }, collection)
// }

// /**
// * Stack a list of cards into a list of stacked counted cards by their id
// * @param collection Optional. The collection count in which to also provide the collection count to the stacked card
// * @return list of stacked cards
// */
// fun <CardT> List<CardT>.stack(collection: Collection<CardT>? = null): List<StackedCard<CardT>> {
//  return stack({ it.id }, collection)
// }

/**
 * Stack a list of cards into a list of stacked counted cards
 * @param selector the selector for choosing the key from a card in which to group them
 * @param collection Optional. The collection count in which to also provide the collection count to the stacked card
 * @return A list of stacked cards
 */
// fun <CardT, KeyK> List<CardT>.stack(
//  selector: (CardT) -> KeyK,
//  collection: Collection<CardT>? = null
// ): List<StackedCard<CardT>> {
//  return groupBy(selector)
//    .mapValues { (_, cards) ->
//      cards.first() to cards.size
//    }
//    .map { (_, countedCard) ->
//      val collectionCount = collection?.getCount(countedCard.first) ?: 0
//      StackedCard(countedCard.first, countedCard.second, collectionCount)
//    }
// }

// /**
// * Unstacked a counted list of cards into a flat list of cards
// * that have duplicates.
// * @return a raw list of cards, unstacked.
// */
// fun <CardT> List<StackedCard<CardT>>.unstack(): List<CardT> = flatMap { stack ->
//  (0 until stack.count).map { stack.card }
// }
