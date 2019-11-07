package com.r0adkll.deckbuilder.arch.data.features.community.model

class DeckTemplatesEntity(
    val tournaments: List<TournamentDeckTemplateEntity> = emptyList(),
    val themeDecks: List<ThemeDeckTemplateEntity> = emptyList()
)
