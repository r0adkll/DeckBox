package app.deckbox.common.resources.strings

expect object CurrencyFormatter {
  fun format(value: Double, type: CurrencyType = CurrencyType.USD): String
}

enum class CurrencyType {
  USD,
  EUR,
}
