package com.r0adkll.deckbuilder.arch.data.features.community.model

import com.r0adkll.deckbuilder.arch.data.features.decks.model.DeckEntity


class TournamentDeckTemplateEntity(
        val author: String = "",
        val rank: Int = 0,
        val deck: DeckEntity? = null,
        val tournament: TournamentEntity? = null
)