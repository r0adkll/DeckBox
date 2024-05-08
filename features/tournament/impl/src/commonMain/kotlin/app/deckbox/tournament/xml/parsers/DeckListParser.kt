package app.deckbox.tournament.xml.parsers

import app.deckbox.core.CurrencyType
import app.deckbox.core.logging.bark
import app.deckbox.tournament.xml.SoupParser
import app.deckbox.tournament.xml.builders.CardBuilder
import app.deckbox.tournament.xml.builders.CardPriceBuilder
import app.deckbox.tournament.xml.builders.DeckListBuilder

class DeckListParser(
  private val deckListBuilder: DeckListBuilder,
) : SoupParser() {
  override fun onOpenTag(name: String, attributes: Map<String, String>, isImplied: Boolean) {
    when (name) {
      "div" -> {
        val clazz = attributes["class"] ?: return
        when (clazz) {
          "decklist-title" -> push(DeckListTitleParser(deckListBuilder))
          "decklist-card" -> {
            val cardNumber = attributes["data-number"] ?: return
            val cardSetCode = attributes["data-set"] ?: return
            push(DeckListCardParser(cardNumber, cardSetCode, deckListBuilder))
          }
        }
      }
    }
  }
}

class DeckListTitleParser(
  private val deckListBuilder: DeckListBuilder,
) : SoupParser() {

  override fun onOpenTag(name: String, attributes: Map<String, String>, isImplied: Boolean) {
    when (name) {
      "a" -> {
        val clazz = attributes["class"] ?: return
        if (clazz.contains("decklist-price") && clazz.contains("usd")) {
          val bulkPurchaseUrl = attributes["href"] ?: return
          deckListBuilder.bulkPurchaseUrl = bulkPurchaseUrl
          push(
            PriceParser(
              tagName = "a",
              onPrice = { price -> deckListBuilder.price[CurrencyType.USD] = price },
            ),
          )
        }
      }

      "span" -> {
        val clazz = attributes["class"] ?: return
        if (clazz.contains("decklist-price") && clazz.contains("usd")) {
          push(
            PriceParser(
              tagName = "span",
              onPrice = { price -> deckListBuilder.price[CurrencyType.EUR] = price },
            ),
          )
        }
      }
    }
  }

  override fun onText(text: String) {
    deckListBuilder.deckName = text
  }

  override fun onCloseTag(name: String, isImplied: Boolean) {
    when (name) {
      "div" -> pop()
    }
  }
}

class DeckListCardParser(
  cardNumber: String,
  setCode: String,
  private val deckListBuilder: DeckListBuilder,
) : SoupParser() {

  private val cardBuilder = CardBuilder(
    setCode = setCode,
    number = cardNumber,
  )

  override fun onOpenTag(name: String, attributes: Map<String, String>, isImplied: Boolean) {
    when (name) {
      "span" -> {
        val clazz = attributes["class"] ?: return
        when (clazz) {
          "card-count" -> push(TextParser("span") { cardBuilder.count = it.toIntOrNull() })
          "card-name" -> push(TextParser("span") { cardBuilder.name = it })
        }
      }
      "a" -> {
        val clazz = attributes["class"] ?: return
        when {
          clazz.contains("card-price") -> {
            val purchaseUrl = attributes["href"] ?: return
            when {
              clazz.contains("usd") -> {
                val priceBuilder = CardPriceBuilder(CurrencyType.USD, url = purchaseUrl)
                push(
                  PriceParser("a") { price ->
                    priceBuilder.amount = price
                    cardBuilder.prices[CurrencyType.USD] = priceBuilder
                  },
                )
              }
              clazz.contains("eur") -> {
                val priceBuilder = CardPriceBuilder(CurrencyType.EUR, url = purchaseUrl)
                push(
                  PriceParser("a") { price ->
                    priceBuilder.amount = price
                    cardBuilder.prices[CurrencyType.EUR] = priceBuilder
                  },
                )
              }
            }
          }
        }
      }
    }
  }

  override fun onCloseTag(name: String, isImplied: Boolean) {
    when (name) {
      "div" -> {
        cardBuilder.build()?.let { card ->
          deckListBuilder.cards += card
        }
        pop()
      }
    }
  }
}

class PriceParser(
  private val tagName: String,
  private val onPrice: (Double) -> Unit,
) : SoupParser() {
  override fun onText(text: String) {
    val trimmedText = text.trim()
    bark { "PriceParser:onText($trimmedText)" }
    PriceRegex.matchEntire(trimmedText)?.let { match ->
      match.groupValues.getOrNull(1)?.toDoubleOrNull()?.let(onPrice)
        ?: bark { "Unable to find price in RegEx for ${match.groupValues}" }
    }
  }

  override fun onCloseTag(name: String, isImplied: Boolean) {
    if (name == tagName) pop()
  }

  companion object {
    private val PriceRegex = "[\$€]?(\\d+.\\d+)[\$€]?".toRegex()
  }
}

class TextParser(
  private val tagName: String,
  private val onValue: (String) -> Unit,
) : SoupParser() {
  override fun onText(text: String) {
    onValue(text)
  }

  override fun onCloseTag(name: String, isImplied: Boolean) {
    if (name == tagName) pop()
  }
}
