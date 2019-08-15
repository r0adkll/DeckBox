package com.r0adkll.deckbuilder.arch.data.database.entities

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "decks")
class DeckEntity(
        @PrimaryKey(autoGenerate = true) var uid: Long,
        var name: String,
        var description: String?,
        var image: Uri?,
        var collectionOnly: Boolean = false,
        var timestamp: Long
)