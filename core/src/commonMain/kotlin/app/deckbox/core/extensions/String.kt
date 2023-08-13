package app.deckbox.core.extensions

fun String.capitalized(): String {
  return lowercase()
    .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
