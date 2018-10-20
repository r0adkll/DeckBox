package com.r0adkll.deckbuilder.arch.data.databasev2.entities

import androidx.room.Embedded


class SessionCardEntity(
        var count: Int,
        @Embedded var card: CardEntity
)