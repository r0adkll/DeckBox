package app.deckbox.common.resources.strings

import app.deckbox.core.CurrencyType

expect object CurrencyFormatter {
  fun format(value: Double, type: CurrencyType = CurrencyType.USD): String
}
