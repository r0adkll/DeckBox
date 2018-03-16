package com.r0adkll.deckbuilder.arch.data.features.decks.model



class ExpansionEntity(
        val code: String = "",
        val ptcgoCode: String? = "",
        val name: String = "",
        val series: String = "",
        val totalCards: Int = 0,
        val standardLegal: Boolean = false,
        val expandedLegal: Boolean = false,
        val releaseDate: String = "",
        val symbolUrl: String = "",
        val logoUrl: String? = ""
)