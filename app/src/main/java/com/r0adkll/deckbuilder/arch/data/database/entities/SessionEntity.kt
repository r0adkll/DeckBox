package com.r0adkll.deckbuilder.arch.data.database.entities

import android.net.Uri
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
        tableName = "sessions",
        indices = [
            Index("deckId", unique = true)
        ]
)
class SessionEntity(
        @PrimaryKey(autoGenerate = true) var uid: Long,
        var deckId: String?,

        var originalName: String?,
        var originalDescription: String?,
        var originalImage: Uri?,
        var originalCollectionOnly: Boolean?,

        var name: String?,
        var description: String?,
        var image: Uri?,
        var collectionOnly: Boolean?
)