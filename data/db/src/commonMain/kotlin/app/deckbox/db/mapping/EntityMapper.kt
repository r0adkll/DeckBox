package app.deckbox.db.mapping

import app.deckbox.core.model.Card
import app.deckbox.core.model.Expansion
import app.deckbox.core.model.Legalities
import app.deckbox.sqldelight.Abilities
import app.deckbox.sqldelight.Attacks
import app.deckbox.sqldelight.CardMarketPrices
import app.deckbox.sqldelight.Cards
import app.deckbox.sqldelight.Expansions
import app.deckbox.sqldelight.TcgPlayerPrices

fun Cards.toModel(
  expansion: Expansions,
  abilities: List<Abilities>,
  attacks: List<Attacks>,
  tcgPlayerPrices: TcgPlayerPrices?,
  cardMarketPrices: CardMarketPrices?,
): Card {
  return Card(
    id = id,
    name = name,
    image = Card.Image(
      small = image_small,
      large = image_large,
    ),
    supertype = supertype,
    subtypes = subtypes,
    level = level,
    hp = hp,
    types = types,
    evolvesFrom = evolvesFrom,
    evolvesTo = evolvesTo,
    rules = rules,
    ancientTrait = if (ancientTrait_name != null && ancientTrait_text != null) {
      Card.Ability(ancientTrait_name, ancientTrait_text, ancientTrait_type)
    } else {
      null
    },
    abilities = abilities.map { it.toModel() },
    attacks = attacks.map { it.toModel() },
    weaknesses = weaknesses,
    resistances = resistances,
    retreatCost = retreatCost,
    convertedRetreatCost = convertedRetreatCost,
    number = number,
    expansion = expansion.toModel(),
    artist = artist,
    rarity = rarity,
    flavorText = flavorText,
    nationalPokedexNumbers = nationalPokedexNumbers,
    legalities = Legalities(
      standard = legalitiesStandard,
      expanded = legalitiesExpanded,
      unlimited = legalitiesUnlimited,
    ),
    tcgPlayer = tcgPlayerPrices?.toModel(),
    cardMarket = cardMarketPrices?.toModel(),
  )
}

fun TcgPlayerPrices.toModel(): Card.TcgPlayer {
  return Card.TcgPlayer(
    url = url,
    updatedAt = updatedAt,
    prices = Card.TcgPlayer.Prices(
      normal = toNormalPrice(),
      holofoil = toHolofoil(),
      reverseHolofoil = toReverseHolofoil(),
      firstEditionHolofoil = to1stEditionHolofoil(),
      firstEditionNormal = to1stEditionNormal(),
    ),
  )
}

fun TcgPlayerPrices.toNormalPrice(): Card.TcgPlayer.Price? {
  return if (normalLow != null || normalMid != null || normalHigh != null || normalMarket != null) {
    Card.TcgPlayer.Price(
      low = normalLow,
      mid = normalMid,
      high = normalHigh,
      market = normalMarket,
      directLow = normalDirectLow,
    )
  } else {
    null
  }
}

fun TcgPlayerPrices.toHolofoil(): Card.TcgPlayer.Price? {
  return if (holofoilLow != null || holofoilMid != null || holofoilHigh != null || holofoilMarket != null) {
    Card.TcgPlayer.Price(
      low = holofoilLow,
      mid = holofoilMid,
      high = holofoilHigh,
      market = holofoilMarket,
      directLow = holofoilDirectLow,
    )
  } else {
    null
  }
}

fun TcgPlayerPrices.toReverseHolofoil(): Card.TcgPlayer.Price? {
  return if (reverseHolofoilLow != null || reverseHolofoilMid != null ||
    reverseHolofoilHigh != null || reverseHolofoilMarket != null
  ) {
    Card.TcgPlayer.Price(
      low = reverseHolofoilLow,
      mid = reverseHolofoilMid,
      high = reverseHolofoilHigh,
      market = reverseHolofoilMarket,
      directLow = reverseHolofoilDirectLow,
    )
  } else {
    null
  }
}

fun TcgPlayerPrices.to1stEditionHolofoil(): Card.TcgPlayer.Price? {
  return if (firstEditionHolofoilLow != null || firstEditionHolofoilMid != null ||
    firstEditionHolofoilHigh != null || firstEditionHolofoilMarket != null
  ) {
    Card.TcgPlayer.Price(
      low = firstEditionHolofoilLow,
      mid = firstEditionHolofoilMid,
      high = firstEditionHolofoilHigh,
      market = firstEditionHolofoilMarket,
      directLow = firstEditionHolofoilDirectLow,
    )
  } else {
    null
  }
}

fun TcgPlayerPrices.to1stEditionNormal(): Card.TcgPlayer.Price? {
  return if (firstEditionNormalLow != null || firstEditionNormalMid != null ||
    firstEditionNormalHigh != null || firstEditionNormalMarket != null
  ) {
    Card.TcgPlayer.Price(
      low = firstEditionNormalLow,
      mid = firstEditionNormalMid,
      high = firstEditionNormalHigh,
      market = firstEditionNormalMarket,
      directLow = firstEditionNormalDirectLow,
    )
  } else {
    null
  }
}

fun CardMarketPrices.toModel(): Card.CardMarket {
  return Card.CardMarket(
    url = url,
    updatedAt = updatedAt,
    prices = Card.CardMarket.Prices(
      averageSellPrice = averageSellPrice,
      lowPrice = lowPrice,
      trendPrice = trendPrice,
      germanProLow = germanProLow,
      suggestedPrice = suggestedPrice,
      reverseHoloSell = reverseHoloSell,
      reverseHoloLow = reverseHoloLow,
      reverseHoloTrend = reverseHoloTrend,
      lowPriceExPlus = lowPriceExPlus,
      avg1 = avg1,
      avg7 = avg7,
      avg30 = avg30,
      reverseHoloAvg1 = reverseHoloAvg1,
      reverseHoloAvg7 = reverseHoloAvg7,
      reverseHoloAvg30 = reverseHoloAvg30,
    ),
  )
}

fun Abilities.toModel(): Card.Ability = Card.Ability(
  name = name,
  text = text,
  type = type,
)

fun Attacks.toModel(): Card.Attack = Card.Attack(
  cost = cost,
  name = name,
  text = text,
  damage = damage,
  convertedEnergyCost = convertedEnergyCost,
)

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
