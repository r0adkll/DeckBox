package com.r0adkll.deckbuilder.arch.data.features.subtypes.cache

class InMemorySubtypeCache(
  private val expirationInMs: Long = 86_400_000L // 1 Day
) : SubtypeCache {

  private var cachedTime = 0L
  private var subtypes = emptyList<String>()

  override fun get(): List<String> {
    return synchronized(this) {
      if (System.currentTimeMillis() - cachedTime >= expirationInMs) {
        subtypes = emptyList()
        cachedTime = 0L
      }
      subtypes
    }
  }

  override fun put(values: List<String>) {
    synchronized(this) {
      cachedTime = System.currentTimeMillis()
      subtypes = values
    }
  }

  override fun clear() {
    synchronized(this) {
      subtypes = emptyList()
    }
  }
}
