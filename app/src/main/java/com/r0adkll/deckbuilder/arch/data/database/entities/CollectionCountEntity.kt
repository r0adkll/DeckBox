package com.r0adkll.deckbuilder.arch.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collection")
class CollectionCountEntity(
    @PrimaryKey var cardId: String,
    var count: Int,
    var set: String,
    var series: String
)
