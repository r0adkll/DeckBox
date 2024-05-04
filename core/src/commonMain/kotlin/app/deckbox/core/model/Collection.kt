package app.deckbox.core.model

data class Collection<Key>(
  private val counts: Map<Key, Int>,
) {

  val total: Int by lazy { counts.count { (_, count) -> count > 0 } }

  operator fun get(key: Key): Int {
    return counts[key] ?: 0
  }

  companion object {
    fun <T : Any> empty(): Collection<T> = Collection(emptyMap())
  }
}
