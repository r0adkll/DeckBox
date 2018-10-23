package com.r0adkll.deckbuilder.arch.data.databasev2.relations

import androidx.room.Embedded
import com.r0adkll.deckbuilder.arch.data.databasev2.entities.CardEntity


class SessionCard(
        var count: Int,
        @Embedded var card: CardWithAttacks
)