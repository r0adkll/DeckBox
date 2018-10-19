package com.r0adkll.deckbuilder.arch.data.databasev2.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
        tableName = "session_changes",
        foreignKeys = [ForeignKey(
                entity = SessionEntity::class,
                parentColumns = ["uid"],
                childColumns = ["sessionId"],
                onDelete = ForeignKey.CASCADE
        )]
)
class SessionChangeEntity(
        @PrimaryKey(autoGenerate = true) var uid: Long,
        var sessionId: Long,

        var cardId: String,
        var change: Int, // 1 - Add; -1 - Remove
        var searchSessionId: String?
)