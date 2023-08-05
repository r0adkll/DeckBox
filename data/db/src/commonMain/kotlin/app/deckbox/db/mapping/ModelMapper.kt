package app.deckbox.db.mapping

import app.deckbox.core.model.Card
import app.deckbox.core.model.Expansion
import app.deckbox.sqldelight.Abilities
import app.deckbox.sqldelight.Attacks
import app.deckbox.sqldelight.Cards
import app.deckbox.sqldelight.Expansions

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
  tcgPlayerUrl = tcgPlayer?.url,
  tcgPlayerUpdatedAt = tcgPlayer?.updatedAt,
  tcgPlayerLow = tcgPlayer?.prices?.low,
  tcgPlayerMid = tcgPlayer?.prices?.mid,
  tcgPlayerHigh = tcgPlayer?.prices?.high,
  tcgPlayerMarket = tcgPlayer?.prices?.market,
  tcgPlayerDirectLow = tcgPlayer?.prices?.directLow,
  cardMarketUrl = cardMarket?.url,
  cardMarketUpdatedAt = cardMarket?.updatedAt,
  cardMarketAverageSellPrice = cardMarket?.prices?.averageSellPrice,
  cardMarketLowPrice = cardMarket?.prices?.lowPrice,
  cardMarketTrendPrice = cardMarket?.prices?.trendPrice,
  cardMarketGermanProLow = cardMarket?.prices?.germanProLow,
  cardMarketSuggestedPrice = cardMarket?.prices?.suggestedPrice,
  cardMarketReverseHoloSell = cardMarket?.prices?.reverseHoloSell,
  cardMarketReverseHoloLow = cardMarket?.prices?.reverseHoloLow,
  cardMarketReverseHoloTrend = cardMarket?.prices?.reverseHoloTrend,
  cardMarketLowPriceExPlus = cardMarket?.prices?.lowPriceExPlus,
  cardMarketAvg1 = cardMarket?.prices?.avg1,
  cardMarketAvg7 = cardMarket?.prices?.avg7,
  cardMarketAvg30 = cardMarket?.prices?.avg30,
  cardMarketReverseHoloAvg1 = cardMarket?.prices?.reverseHoloAvg1,
  cardMarketReverseHoloAvg7 = cardMarket?.prices?.reverseHoloAvg7,
  cardMarketReverseHoloAvg30 = cardMarket?.prices?.reverseHoloAvg30,
  expansionId = expansion.id,
)

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
