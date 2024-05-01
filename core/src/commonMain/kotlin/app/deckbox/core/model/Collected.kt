package app.deckbox.core.model

/**
 * A collection wrapper to attach a sum count for a wrapped item [T]
 */
data class Collected<T>(
  val item: T,
  val count: Int,
)

fun <T> T.collected(count: Int): Collected<T> = Collected(this, count)
