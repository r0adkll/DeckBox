package app.deckbox.core.extensions

fun <T> List<T>.prependIfNotEmpty(item: T): List<T> {
  return if (isEmpty()) this else listOf(item) + this
}

fun <T> List<T>.prependFilterIfNotEmpty(
  predicate: (T) -> Boolean,
  item: T
): List<T> {
  val filtered = filter(predicate)
  return if (filtered.isEmpty()) this else listOf(item) + this
}
