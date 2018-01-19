package com.r0adkll.deckbuilder.arch.domain.features.cards.model


enum class SearchField {
    NAME,
    TEXT,
    ABILITY_NAME,
    ABILITY_TEXT,
    ATTACK_NAME,
    ATTACK_TEXT;

    companion object {
        val VALUES by lazy { values() }
    }
}