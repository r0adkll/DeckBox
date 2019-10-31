package com.r0adkll.deckbuilder.arch.data.database.relations

import androidx.room.Embedded

class DeckStackedCard(
        var deckId: Long,
        var count: Int,
        @Embedded
        var card: CardWithAttacks
)
