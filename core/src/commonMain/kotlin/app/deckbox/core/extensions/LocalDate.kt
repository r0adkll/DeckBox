package app.deckbox.core.extensions

import kotlinx.datetime.LocalDate

val LocalDate.readableFormat: String
  get() = "${month.name.capitalized()} $dayOfMonth, $year"
