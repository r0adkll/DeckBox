package app.deckbox.core.extensions

fun Int.formattedPlace(): String = when (this) {
  1 -> "${this}st"
  2 -> "${this}nd"
  3 -> "${this}rd"
  else -> "${this}th"
}
