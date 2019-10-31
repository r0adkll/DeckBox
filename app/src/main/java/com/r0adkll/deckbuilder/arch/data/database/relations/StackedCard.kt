package com.r0adkll.deckbuilder.arch.data.database.relations

import androidx.room.Embedded

open class StackedCard(
        var count: Int,
        @Embedded var card: CardWithAttacks
)
