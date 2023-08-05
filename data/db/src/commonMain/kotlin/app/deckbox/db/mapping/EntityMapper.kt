package app.deckbox.db.mapping

import app.deckbox.core.model.Card
import app.deckbox.core.model.Expansion
import app.deckbox.core.model.Legalities
import app.deckbox.sqldelight.Abilities
import app.deckbox.sqldelight.Attacks
import app.deckbox.sqldelight.Cards
import app.deckbox.sqldelight.Expansions

fun Cards.toModel(
  expansion: Expansions,
  abilities: List<Abilities>,
  attacks: List<Attacks>,
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
    tcgPlayer = toTcgPlayer(),
    cardMarket = toCardMarket(),
  )
}

fun Cards.toTcgPlayer(): Card.TcgPlayer? {
  return if (tcgPlayerUrl != null) {
    Card.TcgPlayer(
      url = tcgPlayerUrl,
      updatedAt = tcgPlayerUpdatedAt,
      prices = Card.TcgPlayer.Prices(
        low = tcgPlayerLow,
        mid = tcgPlayerMid,
        high = tcgPlayerHigh,
        market = tcgPlayerMarket,
        directLow = tcgPlayerDirectLow,
      ),
    )
  } else {
    null
  }
}

fun Cards.toCardMarket(): Card.CardMarket? {
  return if (cardMarketUrl != null) {
    Card.CardMarket(
      url = cardMarketUrl,
      updatedAt = cardMarketUpdatedAt,
      prices = Card.CardMarket.Prices(
        averageSellPrice = cardMarketAverageSellPrice,
        lowPrice = cardMarketLowPrice,
        trendPrice = cardMarketTrendPrice,
        germanProLow = cardMarketGermanProLow,
        suggestedPrice = cardMarketSuggestedPrice,
        reverseHoloSell = cardMarketReverseHoloSell,
        reverseHoloLow = cardMarketReverseHoloLow,
        reverseHoloTrend = cardMarketReverseHoloTrend,
        lowPriceExPlus = cardMarketLowPriceExPlus,
        avg1 = cardMarketAvg1,
        avg7 = cardMarketAvg7,
        avg30 = cardMarketAvg30,
        reverseHoloAvg1 = cardMarketReverseHoloAvg1,
        reverseHoloAvg7 = cardMarketReverseHoloAvg7,
        reverseHoloAvg30 = cardMarketReverseHoloAvg30,
      ),
    )
  } else {
    null
  }
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
