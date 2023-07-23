package app.deckbox.network.api

import app.deckbox.core.model.Card
import app.deckbox.core.model.Expansion
import app.deckbox.core.model.Legalities
import app.deckbox.core.model.Legality
import app.deckbox.core.model.SuperType
import app.deckbox.core.model.Type
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime

internal object ModelMapper {

  fun toCards(models: Iterable<CardModel>): List<Card> {
    return models.map { to(it) }
  }

  fun to(model: CardModel): Card {
    return Card(
      id = model.id,
      name = model.name,
      supertype = SuperType.find(model.supertype),
      subtypes = model.subtypes ?: emptyList(),
      level = model.level,
      hp = model.hp,
      types = model.types?.map { Type.find(it) },
      evolvesFrom = model.evolvesFrom,
      evolvesTo = model.evolvesTo,
      rules = model.rules,
      ancientTrait = model.ancientTrait?.let { to(it) },
      abilities = model.abilities?.map { to(it) },
      attacks = model.attacks?.map { to(it) },
      weaknesses = model.weaknesses?.map { to(it) },
      resistances = model.resistances?.map { to(it) },
      retreatCost = model.retreatCost?.map { Type.find(it) },
      convertedRetreatCost = model.convertedRetreatCost,
      number = model.number,
      expansion = to(model.set),
      artist = model.artist,
      rarity = model.rarity,
      flavorText = model.flavorText,
      nationalPokedexNumbers = model.nationalPokedexNumbers,
      legalities = model.legalities?.let { to(it) },
      image = Card.Image(
        small = model.images.small,
        large = model.images.large,
      ),
      tcgPlayer = model.tcgplayer?.let { to(it) },
      cardMarket = model.cardmarket?.let { to(it) },
    )
  }

  private fun to(model: AbilityModel): Card.Ability {
    return Card.Ability(
      name = model.name,
      text = model.text,
      type = model.type
    )
  }

  private fun to(model: AttackModel): Card.Attack {
    return Card.Attack(
      cost = model.cost?.map { Type.find(it) },
      name = model.name,
      text = model.text,
      damage = model.damage,
      convertedEnergyCost = model.convertedEnergyCost
    )
  }

  private fun to(model: EffectModel): Card.Effect {
    return Card.Effect(Type.find(model.type), model.value)
  }

  fun toSets(models: Iterable<CardSetModel>): List<Expansion> {
    return models.map { to(it) }
  }

  fun to(model: CardSetModel): Expansion {
    return Expansion(
      id = model.id,
      name = model.name,
      series = model.series,
      printedTotal = model.printedTotal,
      total = model.total,
      legalities = Legalities(
        unlimited = Legality.from(model.legalities.unlimited),
        standard = Legality.from(model.legalities.standard),
        expanded = Legality.from(model.legalities.expanded),
      ),
      ptcgoCode = model.ptcgoCode,
      releaseDate = model.releaseDate.toLocalDate(),
      updatedAt = model.updatedAt.toLocalDateTime(),
      images = Expansion.Images(
        symbol = model.images.symbol,
        logo = model.images.logo,
      )
    )
  }

  private fun to(model: LegalitiesModel): Legalities {
    return Legalities(
      unlimited = Legality.from(model.unlimited),
      standard = Legality.from(model.standard),
      expanded = Legality.from(model.expanded),
    )
  }

  private fun to(model: TcgPlayerModel): Card.TcgPlayer {
    return Card.TcgPlayer(
      url = model.url,
      updatedAt = model.updated,
      prices = model.prices?.let { prices ->
        Card.TcgPlayer.Prices(
          low = prices.low,
          mid = prices.mid,
          high = prices.high,
          market = prices.market,
          directLow = prices.directLow,
        )
      }
    )
  }

  private fun to(model: CardMarketModel): Card.CardMarket {
    return Card.CardMarket(
      url = model.url,
      updatedAt = model.updatedAt,
      prices = model.prices?.let { prices ->
        Card.CardMarket.Prices(
          averageSellPrice = prices.averageSellPrice,
          lowPrice = prices.lowPrice,
          trendPrice = prices.trendPrice,
          germanProLow = prices.germanProLow,
          suggestedPrice = prices.suggestedPrice,
          reverseHoloSell = prices.reverseHoloSell,
          reverseHoloLow = prices.reverseHoloLow,
          reverseHoloTrend = prices.reverseHoloTrend,
          lowPriceExPlus = prices.lowPriceExPlus,
          avg1 = prices.avg1,
          avg7 = prices.avg7,
          avg30 = prices.avg30,
          reverseHoloAvg1 = prices.reverseHoloAvg1,
          reverseHoloAvg7 = prices.reverseHoloAvg7,
          reverseHoloAvg30 = prices.reverseHoloAvg30,
        )
      }
    )
  }
}
