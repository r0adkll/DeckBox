package app.deckbox.core.extensions

fun <T> List<T>.prependIfEmpty(item: T): List<T> {
  return if (isEmpty()) this else listOf(item) + this
}
