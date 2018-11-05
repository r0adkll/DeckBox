package com.r0adkll.deckbuilder.arch.domain.features.remote.model


data class Reprints(
        val standardHashes: Set<Long>,
        val expandedHashes: Set<Long>
)