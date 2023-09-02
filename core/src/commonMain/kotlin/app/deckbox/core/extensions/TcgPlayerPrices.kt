package app.deckbox.core.extensions

import app.deckbox.core.model.Card

fun Card.TcgPlayer.Prices.lowestMarketPrice(): Double? {
  var lowestPrice = Double.MAX_VALUE
  fun Card.TcgPlayer.Price.checkLowest() {
    market?.let {
      if (it < lowestPrice) lowestPrice = it
    }
  }

  normal?.checkLowest()
  holofoil?.checkLowest()
  reverseHolofoil?.checkLowest()
  firstEditionNormal?.checkLowest()
  firstEditionHolofoil?.checkLowest()

  return lowestPrice.takeIf { it != Double.MAX_VALUE }
}
