package app.deckbox.tournament.db

import app.deckbox.core.CurrencyType
import app.deckbox.sqldelight.Deck_list_cards
import app.deckbox.sqldelight.Deck_lists
import app.deckbox.sqldelight.GetCardsForDeckList
import app.deckbox.sqldelight.Participants
import app.deckbox.sqldelight.Tournaments
import app.deckbox.tournament.api.model.DeckArchetype
import app.deckbox.tournament.api.model.DeckList
import app.deckbox.tournament.api.model.Participant
import app.deckbox.tournament.api.model.Tournament

fun Tournaments.toEntity(): Tournament {
  return Tournament(
    id = id,
    name = name,
    date = date,
    country = country,
    format = format,
    participantCount = participantCount,
    winner = Tournament.Winner(winnerId, winnerName),
  )
}

fun Tournament.toModel(): Tournaments {
  return Tournaments(
    id = id,
    name = name,
    date = date,
    country = country,
    format = format,
    participantCount = participantCount,
    winnerId = winner.id,
    winnerName = winner.name,
  )
}

fun Participants.toEntity(): Participant {
  return Participant(
    id = id,
    name = name,
    country = country,
    place = place,
    archetype = DeckArchetype(
      id = archetypeId,
      name = archetypeName,
      variant = archetypeVariant,
      symbols = archetypeSymbols,
    ),
    deckListId = deckListId,
  )
}

fun Participant.toModel(tournamentId: String): Participants {
  return Participants(
    id = id,
    name = name,
    country = country,
    place = place,
    archetypeId = archetype.id,
    archetypeName = archetype.name,
    archetypeVariant = archetype.variant,
    archetypeSymbols = archetype.symbols,
    deckListId = deckListId,
    tournamentId = tournamentId,
  )
}

fun Deck_lists.toEntity(cards: List<GetCardsForDeckList>): DeckList {
  return DeckList(
    id = id,
    name = name,
    price = buildMap {
      priceUsd?.let { put(CurrencyType.USD, it) }
      priceEur?.let { put(CurrencyType.EUR, it) }
    },
    bulkPurchaseUrl = purchaseUrl,
    cards = cards.map { it.toEntity() },
  )
}

fun DeckList.toModel(): Deck_lists {
  return Deck_lists(
    id = id,
    name = name,
    priceUsd = price[CurrencyType.USD],
    priceEur = price[CurrencyType.EUR],
    purchaseUrl = bulkPurchaseUrl,
  )
}

fun DeckList.Card.toModel(): Deck_list_cards {
  return Deck_list_cards(
    id = key,
    name = name,
    setCode = setCode,
    number = number,
    priceUsd = prices[CurrencyType.USD]?.amount,
    priceUsdUrl = prices[CurrencyType.USD]?.url,
    priceEur = prices[CurrencyType.EUR]?.amount,
    priceEurUrl = prices[CurrencyType.EUR]?.url,
  )
}

fun GetCardsForDeckList.toEntity(): DeckList.Card {
  return DeckList.Card(
    name = name,
    count = count,
    setCode = setCode,
    number = number,
    prices = buildMap {
      priceUsd?.let { put(CurrencyType.USD, DeckList.Card.Price(it, CurrencyType.USD, priceUsdUrl)) }
      priceEur?.let { put(CurrencyType.EUR, DeckList.Card.Price(it, CurrencyType.EUR, priceUsdUrl)) }
    },
  )
}
