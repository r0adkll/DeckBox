package com.r0adkll.deckbuilder.arch.domain.features.cards.model


sealed class CacheStatus {
    object Empty : CacheStatus()
    object Cached : CacheStatus()
    object Deleting : CacheStatus()
    class Downloading(val count: Int) : CacheStatus()
}