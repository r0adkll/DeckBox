package app.deckbox.expansions.db

import app.deckbox.core.model.Expansion
import app.deckbox.core.model.Legalities
import app.deckbox.sqldelight.Expansions

fun Iterable<Expansions>.toModels(): List<Expansion> = map(Expansions::toModel)

fun Expansions.toModel(): Expansion = Expansion(
  id = id,
  name = name,
  total = total,
  printedTotal = printedTotal,
  series = series,
  ptcgoCode = ptcgoCode,
  legalities = Legalities(
    unlimited = legalitiesUnlimited,
    standard = legalitiesStandard,
    expanded = legalitiesExpanded,
  ),
  images = Expansion.Images(
    symbol = symbol,
    logo = logo,
  ),
  releaseDate = releaseDate,
  updatedAt = updatedAt,
)

fun Expansion.toEntity(): Expansions = Expansions(
  id = id,
  name = name,
  total = total,
  printedTotal = printedTotal,
  series = series,
  ptcgoCode = ptcgoCode,
  legalitiesUnlimited = legalities?.unlimited,
  legalitiesStandard = legalities?.standard,
  legalitiesExpanded = legalities?.expanded,
  symbol = images.symbol,
  logo = images.logo,
  releaseDate = releaseDate,
  updatedAt = updatedAt,
)
