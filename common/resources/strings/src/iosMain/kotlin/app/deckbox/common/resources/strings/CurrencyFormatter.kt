package app.deckbox.common.resources.strings

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle

actual object CurrencyFormatter {
  actual fun format(value: Double, type: CurrencyType): String {
    val currencyFormatter = NSNumberFormatter()
    currencyFormatter.usesGroupingSeparator = true
    currencyFormatter.numberStyle = NSNumberFormatterCurrencyStyle
    currencyFormatter.setCurrencyCode(when(type) {
      CurrencyType.USD -> "usd"
      CurrencyType.EUR -> "eur"
    })
    return currencyFormatter.stringFromNumber(NSNumber(value))
      ?: "$value"
  }
}
