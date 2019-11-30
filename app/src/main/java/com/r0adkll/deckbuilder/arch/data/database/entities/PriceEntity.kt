package com.r0adkll.deckbuilder.arch.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "marketplace_prices",
    foreignKeys = [ForeignKey(
        entity = ProductEntity::class,
        parentColumns = ["id"],
        childColumns = ["productId"],
        onDelete = ForeignKey.CASCADE
    )]
)
class PriceEntity(
    @PrimaryKey(autoGenerate = true) var id: Long,
    var rarity: String,
    var low: Double? = null,
    var mid: Double? = null,
    var high: Double? = null,
    var market: Double? = null,
    var directLow: Double? = null,
    var updatedAt: Long,
    var expiresAt: Long,

    var productId: Long
)
