package app.deckbox.db.mapping

import app.deckbox.core.model.BoosterPack
import app.deckbox.sqldelight.Booster_packs
import app.deckbox.sqldelight.boosterPack.GetAll
import app.deckbox.sqldelight.boosterPack.GetById

fun GetAll.toModel(): BoosterPack {
  return BoosterPack(
    id = id,
    name = name,
    cardImages = cardImages?.split(",")?.toList() ?: emptyList(),
    updatedAt = updatedAt,
    createdAt = createdAt,
  )
}

fun GetById.toModel(): BoosterPack {
  return BoosterPack(
    id = id,
    name = name,
    cardImages = cardImages?.split(",")?.toList() ?: emptyList(),
    updatedAt = updatedAt,
    createdAt = createdAt,
  )
}

fun GetById.asBoosterPacks(): Booster_packs = Booster_packs(
  id = id,
  name = name,
  createdAt = createdAt,
  updatedAt = updatedAt,
)
