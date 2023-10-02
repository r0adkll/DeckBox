package app.deckbox.core.extensions

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun <T> List<T>.prependIfNotEmpty(item: T): List<T> {
  return if (isEmpty()) this else listOf(item) + this
}

fun <T> MutableList<T>.addIfEmpty(item: T): MutableList<T> {
  if (isEmpty()) add(item)
  return this
}

fun <T> List<T>.prependFilterIfNotEmpty(
  predicate: (T) -> Boolean,
  item: T,
): List<T> {
  val filtered = filter(predicate)
  return if (filtered.isEmpty()) this else listOf(item) + this
}

fun <T> ImmutableList<T>.shuffle(iterations: Int): ImmutableList<T> {
  val items = this.toMutableList()
  (0 until iterations).forEach { _ ->
    items.shuffle()
  }
  return items.toImmutableList()
}
