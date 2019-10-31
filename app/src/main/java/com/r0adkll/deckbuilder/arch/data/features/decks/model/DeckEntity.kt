package com.r0adkll.deckbuilder.arch.data.features.decks.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

class DeckEntity(
        @Exclude var id: String = "",
        val name: String = "",
        val description: String = "",
        val image: String? = null,
        val collectionOnly: Boolean? = false,

        val cardMetadata: List<CardMetadataEntity>? = null,

        @Deprecated("See Issue#86: Migrating to [Timestamp] object in firebase")
        val timestamp: Long? = null,
        val updatedAt: Timestamp? = null
)
