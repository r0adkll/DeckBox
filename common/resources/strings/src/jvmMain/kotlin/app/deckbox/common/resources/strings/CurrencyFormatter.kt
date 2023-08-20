package app.deckbox.common.resources.strings

import java.text.NumberFormat
import java.util.Currency

actual object CurrencyFormatter {
  actual fun format(value: Double, type: CurrencyType): String {
    return NumberFormat.getCurrencyInstance()
      .apply {
        currency = when (type) {
          CurrencyType.USD -> Currency.getInstance("USD") // These are case-sensitive!
          CurrencyType.EUR -> Currency.getInstance("EUR")
        }
      }
      .format(value)
  }
}
