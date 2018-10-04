package com.r0adkll.deckbuilder.arch.data.remote.model


data class Reprints(
        val standardHashes: Set<Long>,
        val expandedHashes: Set<Long>
)