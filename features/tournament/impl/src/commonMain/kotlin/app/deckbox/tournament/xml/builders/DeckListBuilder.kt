package app.deckbox.tournament.xml.builders

import app.deckbox.core.CurrencyType
import app.deckbox.tournament.api.model.DeckList

class DeckListBuilder {

  var deckName: String? = null
  var price = mutableMapOf<CurrencyType, Double>()
  var bulkPurchaseUrl: String? = null
  var cards = mutableListOf<DeckList.Card>()

  fun build(): DeckList? {
    if (
      deckName != null &&
      bulkPurchaseUrl != null &&
      cards.isNotEmpty()
    ) {
      return DeckList(
        name = deckName!!,
        price = price,
        bulkPurchaseUrl = bulkPurchaseUrl!!,
        cards = cards,
      )
    }
    return null
  }
}

class CardBuilder(
  private val setCode: String,
  private val number: String,
) {
  var count: Int? = null
  var name: String? = null
  var prices = mutableMapOf<CurrencyType, CardPriceBuilder>()

  fun build(): DeckList.Card? {
    if (
      count != null &&
      name != null
    ) {
      return DeckList.Card(
        name = name!!,
        count = count!!,
        setCode = setCode,
        number = number,
        prices = prices.mapNotNull { (type, priceBuilder) ->
          priceBuilder.build()?.let {
            type to it
          }
        }.toMap(),
      )
    }
    return null
  }
}

class CardPriceBuilder(
  val currencyType: CurrencyType,
  var amount: Double? = null,
  var url: String? = null,
) {

  fun build(): DeckList.Card.Price? {
    if (amount != null && url != null) {
      return DeckList.Card.Price(amount!!, currencyType, url!!)
    }
    return null
  }
}
