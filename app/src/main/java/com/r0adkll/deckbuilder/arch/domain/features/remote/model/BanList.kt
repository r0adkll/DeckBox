package com.r0adkll.deckbuilder.arch.domain.features.remote.model

data class BanList(
        val standard: List<String> = emptyList(),
        val expanded: List<String> = emptyList(),
        val unlimited: List<String> = emptyList()
)
