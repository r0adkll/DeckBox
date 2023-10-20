package app.deckbox.tournament.api.model

import app.deckbox.core.CurrencyType

data class DeckList(
  val name: String,
  val price: Price,
  val cards: List<Card>,
) {

  data class Price(
    val amount: Double,
    val currency: CurrencyType,
  )

  data class Card(
    val count: Int,
    val name: String,
    val set: CardSet,
    val prices: Map<CurrencyType, Price>,
  ) {

    data class Price(
      val amount: Double,
      val currency: CurrencyType,
      val url: String,
    )
  }

  data class CardSet(
    val code: String,
    val name: String,
  )
}
