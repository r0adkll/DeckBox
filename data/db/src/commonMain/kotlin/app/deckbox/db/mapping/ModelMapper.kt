package app.deckbox.db.mapping

import app.deckbox.core.model.Card
import app.deckbox.core.model.Expansion
import app.deckbox.sqldelight.Abilities
import app.deckbox.sqldelight.Attacks
import app.deckbox.sqldelight.CardMarketPrices
import app.deckbox.sqldelight.Cards
import app.deckbox.sqldelight.Expansions
import app.deckbox.sqldelight.TcgPlayerPrices
import kotlinx.datetime.LocalDate

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

fun Card.toEntity(): Cards = Cards(
  id = id,
  name = name,
  image_small = image.small,
  image_large = image.large,
  supertype = supertype,
  subtypes = subtypes,
  level = level,
  hp = hp,
  types = types,
  evolvesFrom = evolvesFrom,
  evolvesTo = evolvesTo,
  rules = rules,
  ancientTrait_name = ancientTrait?.name,
  ancientTrait_text = ancientTrait?.text,
  ancientTrait_type = ancientTrait?.type,
  weaknesses = weaknesses,
  resistances = resistances,
  retreatCost = retreatCost,
  convertedRetreatCost = convertedRetreatCost,
  number = number,
  artist = artist,
  rarity = rarity,
  flavorText = flavorText,
  nationalPokedexNumbers = nationalPokedexNumbers,
  legalitiesStandard = legalities?.standard,
  legalitiesExpanded = legalities?.expanded,
  legalitiesUnlimited = legalities?.unlimited,
  expansionId = expansion.id,
)

fun Card.TcgPlayer.toEntity(cardId: String): TcgPlayerPrices {
  return TcgPlayerPrices(
    url = url,
    updatedAt = updatedAt,
    cardId = cardId,

    normalLow = prices?.normal?.low,
    normalMid = prices?.normal?.mid,
    normalHigh = prices?.normal?.high,
    normalMarket = prices?.normal?.market,
    normalDirectLow = prices?.normal?.directLow,

    holofoilLow = prices?.holofoil?.low,
    holofoilMid = prices?.holofoil?.mid,
    holofoilHigh = prices?.holofoil?.high,
    holofoilMarket = prices?.holofoil?.market,
    holofoilDirectLow = prices?.holofoil?.directLow,

    reverseHolofoilLow = prices?.reverseHolofoil?.low,
    reverseHolofoilMid = prices?.reverseHolofoil?.mid,
    reverseHolofoilHigh = prices?.reverseHolofoil?.high,
    reverseHolofoilMarket = prices?.reverseHolofoil?.market,
    reverseHolofoilDirectLow = prices?.reverseHolofoil?.directLow,

    firstEditionHolofoilLow = prices?.firstEditionHolofoil?.low,
    firstEditionHolofoilMid = prices?.firstEditionHolofoil?.mid,
    firstEditionHolofoilHigh = prices?.firstEditionHolofoil?.high,
    firstEditionHolofoilMarket = prices?.firstEditionHolofoil?.market,
    firstEditionHolofoilDirectLow = prices?.firstEditionHolofoil?.directLow,

    firstEditionNormalLow = prices?.firstEditionNormal?.low,
    firstEditionNormalMid = prices?.firstEditionNormal?.mid,
    firstEditionNormalHigh = prices?.firstEditionNormal?.high,
    firstEditionNormalMarket = prices?.firstEditionNormal?.market,
    firstEditionNormalDirectLow = prices?.firstEditionNormal?.directLow,
  )
}

fun Card.CardMarket.toEntity(cardId: String): CardMarketPrices {
  return CardMarketPrices(
    url = url,
    updatedAt = updatedAt,
    cardId = cardId,
    averageSellPrice = prices?.averageSellPrice,
    lowPrice = prices?.lowPrice,
    trendPrice = prices?.trendPrice,
    germanProLow = prices?.germanProLow,
    suggestedPrice = prices?.suggestedPrice,
    reverseHoloSell = prices?.reverseHoloSell,
    reverseHoloLow = prices?.reverseHoloLow,
    reverseHoloTrend = prices?.reverseHoloTrend,
    lowPriceExPlus = prices?.lowPriceExPlus,
    avg1 = prices?.avg1,
    avg7 = prices?.avg7,
    avg30 = prices?.avg30,
    reverseHoloAvg1 = prices?.reverseHoloAvg1,
    reverseHoloAvg7 = prices?.reverseHoloAvg7,
    reverseHoloAvg30 = prices?.reverseHoloAvg30,
  )
}

fun Card.Ability.toEntity(
  cardId: String,
): Abilities = Abilities(
  name = name,
  text = text,
  type = type,
  cardId = cardId,
)

fun Card.Attack.toEntity(
  cardId: String,
): Attacks = Attacks(
  cost = cost,
  name = name,
  text = text,
  damage = damage,
  convertedEnergyCost = convertedEnergyCost,
  cardId = cardId,
)
