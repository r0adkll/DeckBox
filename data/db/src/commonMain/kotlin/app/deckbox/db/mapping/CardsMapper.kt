package app.deckbox.db.mapping

import app.deckbox.core.model.Stacked
import app.deckbox.sqldelight.Cards
import app.deckbox.sqldelight.GetCardsForBoosterPack
import app.deckbox.sqldelight.GetCardsForDeck

fun GetCardsForDeck.toStackedEntity(): Stacked<Cards> {
  return Stacked(
    card = Cards(
      id,
      name,
      image_small,
      image_large,
      supertype,
      subtypes,
      level,
      hp,
      types,
      evolvesFrom,
      evolvesTo,
      rules,
      ancientTrait_name,
      ancientTrait_text,
      ancientTrait_type,
      weaknesses,
      resistances,
      retreatCost,
      convertedRetreatCost,
      number,
      artist,
      rarity,
      flavorText,
      nationalPokedexNumbers,
      legalitiesUnlimited,
      legalitiesStandard,
      legalitiesExpanded,
      expansionId,
    ),
    count = count,
  )
}

fun GetCardsForBoosterPack.toStackedEntity(): Stacked<Cards> {
  return Stacked(
    card = Cards(
      id,
      name,
      image_small,
      image_large,
      supertype,
      subtypes,
      level,
      hp,
      types,
      evolvesFrom,
      evolvesTo,
      rules,
      ancientTrait_name,
      ancientTrait_text,
      ancientTrait_type,
      weaknesses,
      resistances,
      retreatCost,
      convertedRetreatCost,
      number,
      artist,
      rarity,
      flavorText,
      nationalPokedexNumbers,
      legalitiesUnlimited,
      legalitiesStandard,
      legalitiesExpanded,
      expansionId,
    ),
    count = count,
  )
}
