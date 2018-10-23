package com.r0adkll.deckbuilder.arch.data.databasev2.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
        tableName = "session_card_join",
        indices = [Index("sessionId", unique = true), Index("cardId", unique = true)],
        foreignKeys = [
            ForeignKey(
                    entity = SessionEntity::class,
                    parentColumns = ["uid"],
                    childColumns = ["sessionId"]
            ),
            ForeignKey(
                    entity = CardEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["cardId"]
            )
        ]
)
class SessionCardJoin(
        @PrimaryKey(autoGenerate = true) var uid: Long,
        var sessionId: Long,
        var cardId: String,
        var count: Int
)