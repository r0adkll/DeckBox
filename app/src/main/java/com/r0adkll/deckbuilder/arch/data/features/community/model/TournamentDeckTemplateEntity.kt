package com.r0adkll.deckbuilder.arch.data.features.community.model

import com.r0adkll.deckbuilder.arch.data.features.decks.model.CardMetadataEntity

class TournamentDeckTemplateEntity(
    id: String = "",
    val rank: Int = 0,
    val name: String = "",
    val description: String = "",
    val image: String? = null,
    val author: String = "",
    val authorCountry: String = "",
    val deckInfo: List<DeckInfoEntity> = emptyList(),
    val cardMetadata: List<CardMetadataEntity>? = null,
    val tournament: TournamentEntity? = null,
    val timestamp: Long = 0L
) : FirebaseEntity(id)
