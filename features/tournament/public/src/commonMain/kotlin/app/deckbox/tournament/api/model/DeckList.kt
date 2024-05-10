package app.deckbox.tournament.api.model

import app.deckbox.core.CurrencyType

data class DeckList(
  val id: String,
  val name: String,
  val price: Map<CurrencyType, Double>,
  val bulkPurchaseUrl: String?,
  val cards: List<Card>,
) {

  data class Card(
    val name: String,
    val count: Int,
    val setCode: String,
    val number: String,
    val prices: Map<CurrencyType, Price>,
  ) {
    val key: String
      get() = "$setCode-$number"

    data class Price(
      val amount: Double,
      val currency: CurrencyType,
      val url: String?,
    )
  }
}
