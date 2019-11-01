package com.r0adkll.deckbuilder.arch.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "deck_card_join",
    primaryKeys = ["deckId", "cardId"],
    indices = [Index("cardId")],
    foreignKeys = [
        ForeignKey(
            entity = DeckEntity::class,
            parentColumns = ["uid"],
            childColumns = ["deckId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CardEntity::class,
            parentColumns = ["id"],
            childColumns = ["cardId"]
        )
    ]
)
class DeckCardJoin(
    var deckId: Long,
    var cardId: String,
    var count: Int
)
