package com.r0adkll.deckbuilder.arch.data.database.relations

import androidx.room.Embedded


class SessionCard(
        var count: Int,
        @Embedded var card: CardWithAttacks
)