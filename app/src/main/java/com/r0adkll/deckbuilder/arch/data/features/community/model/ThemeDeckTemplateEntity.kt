package com.r0adkll.deckbuilder.arch.data.features.community.model

import com.r0adkll.deckbuilder.arch.data.features.decks.model.CardMetadataEntity

class ThemeDeckTemplateEntity(
    id: String = "",
    val setCode: String = "",
    val name: String = "",
    val description: String = "",
    val image: String? = null,
    val cardMetadata: List<CardMetadataEntity>? = null
) : FirebaseEntity(id)
