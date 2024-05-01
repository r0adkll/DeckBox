package app.deckbox.db.mapping

import app.deckbox.core.model.CollectionCount
import app.deckbox.sqldelight.Collection

fun Collection.toEntity(): CollectionCount {
  return CollectionCount(
    cardId = cardId,
    normalCount = normalCount,
    holofoilCount = holofoilCount,
    reverseHolofoilCount = reverseHolofoilCount,
    firstEditionNormalCount = firstEditionNormalCount,
    firstEditionHolofoilCount = firstEditionHolofoilCount,
  )
}
