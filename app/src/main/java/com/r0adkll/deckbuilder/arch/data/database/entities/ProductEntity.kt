package com.r0adkll.deckbuilder.arch.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "marketplace_products",
    indices = [Index("cardId")]
)
class ProductEntity(
    @ColumnInfo(name = "product_id") @PrimaryKey(autoGenerate = true) var id: Long,
    var cardId: String,
    var setCode: String,
    var groupId: Long,
    var productId: Long,
    var productName: String,
    var url: String,
    var modifiedOn: Long
)
