package app.deckbox.core.extensions

fun String.capitalized(): String {
  return lowercase()
    .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

inline fun String?.ifNullOrBlank(defaultValue: () -> String): String {
  return if (isNullOrBlank()) defaultValue() else this
}
