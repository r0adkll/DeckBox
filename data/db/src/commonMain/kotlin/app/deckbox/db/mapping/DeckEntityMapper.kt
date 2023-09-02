package app.deckbox.db.mapping

import app.deckbox.core.model.Deck
import app.deckbox.core.model.Legalities
import app.deckbox.core.model.Legality
import app.deckbox.sqldelight.Decks
import app.deckbox.sqldelight.deck.GetAll
import app.deckbox.sqldelight.deck.GetById
import kotlinx.datetime.LocalDateTime

fun GetAll.toModel(
  now: LocalDateTime,
): Deck {
  return Deck(
    id = id,
    name = name ?: "",
    description = description,
    tags = tags ?: emptySet(),
    cardImages = cardImages?.split(",")?.toSet() ?: emptySet(),
    legalities = Legalities(
      standard = legalitiesStandard.collectiveLegality(),
      expanded = legalitiesExpanded.collectiveLegality(),
      unlimited = legalitiesUnlimited.collectiveLegality(),
    ),
    createdAt = createdAt ?: now,
    updatedAt = updatedAt ?: now,
  )
}

fun GetById.toModel(
  now: LocalDateTime,
): Deck {
  return Deck(
    id = id,
    name = name ?: "",
    description = description,
    tags = tags ?: emptySet(),
    cardImages = cardImages?.split(",")?.toSet() ?: emptySet(),
    legalities = Legalities(
      standard = legalitiesStandard.collectiveLegality(),
      expanded = legalitiesExpanded.collectiveLegality(),
      unlimited = legalitiesUnlimited.collectiveLegality(),
    ),
    createdAt = createdAt ?: now,
    updatedAt = updatedAt ?: now,
  )
}

fun String?.collectiveLegality(): Legality? {
  val allLegalities = this?.split(",")
    ?.map { Legality.from(it) }
    ?: emptyList()

  return when {
    allLegalities.all { it == Legality.LEGAL } -> Legality.LEGAL
    else -> Legality.ILLEGAL
  }
}

fun GetById.asDecks() = Decks(
  id = id,
  name = name,
  description = description,
  tags = tags,
  createdAt = createdAt,
  updatedAt = updatedAt,
)
