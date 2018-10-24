package com.r0adkll.deckbuilder.arch.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index


@Entity(
        tableName = "session_card_join",
        primaryKeys = ["sessionId", "cardId"],
        indices = [Index("cardId")],
        foreignKeys = [
            ForeignKey(
                    entity = SessionEntity::class,
                    parentColumns = ["uid"],
                    childColumns = ["sessionId"],
                    onDelete = ForeignKey.CASCADE
            ),
            ForeignKey(
                    entity = CardEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["cardId"]
            )
        ]
)
class SessionCardJoin(
        var sessionId: Long,
        var cardId: String,
        var count: Int
)