package com.r0adkll.deckbuilder.arch.data.features.decks.model

import com.google.firebase.firestore.Exclude


class DeckEntity(
        @Exclude var id: String = "",
        val name: String = "",
        val description: String = "",
        val image: String? = null,
        val collectionOnly: Boolean? = false,

        @Deprecated("No longer used", replaceWith = ReplaceWith("cardMetadata"))
        val cards: List<PokemonCardEntity> = emptyList(),

        val cardMetadata: List<CardMetadataEntity>? = null,
        val timestamp: Long = 0L
)