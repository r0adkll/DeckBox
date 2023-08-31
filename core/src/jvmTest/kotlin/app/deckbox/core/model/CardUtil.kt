package app.deckbox.core.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import strikt.api.Assertion

fun card(
  id: String,
  name: String,
  evolvesFrom: String? = null,
  evolvesTo: List<String>? = null,
) = Card(
  id = id,
  name = name,
  evolvesFrom = evolvesFrom,
  evolvesTo = evolvesTo,
  image = Card.Image("", ""),
  supertype = SuperType.POKEMON,
  subtypes = emptyList(),
  level = null,
  hp = null,
  types = null,
  rules = null,
  ancientTrait = null,
  abilities = null,
  attacks = null,
  weaknesses = null,
  resistances = null,
  retreatCost = null,
  convertedRetreatCost = null,
  number = "",
  expansion = Expansion(
    id = "",
    name = "",
    releaseDate = LocalDate(2023, 1, 1),
    total = 0,
    series = "",
    printedTotal = 0,
    legalities = null,
    ptcgoCode = null,
    updatedAt = LocalDateTime(2023, 1, 1, 1, 1, 1, 1),
    images = Expansion.Images("", ""),
  ),
  artist = null,
  rarity = null,
  flavorText = null,
  nationalPokedexNumbers = null,
  legalities = null,
  tcgPlayer = null,
  cardMarket = null,
)

fun Assertion.Builder<Evolution.Node>.hasName(name: String): Assertion.Builder<Evolution.Node> =
  assert("has name %s", name) {
    when (it.name) {
      name -> pass(
        actual = it.name,
        description = "name is %s",
      )

      else -> fail(
        actual = it.name,
        description = "name is %s",
      )
    }
  }
