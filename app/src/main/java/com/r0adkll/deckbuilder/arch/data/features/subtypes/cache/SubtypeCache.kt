package com.r0adkll.deckbuilder.arch.data.features.subtypes.cache

interface SubtypeCache {

  fun get(): List<String>
  fun put(values: List<String>)
  fun clear()
}
