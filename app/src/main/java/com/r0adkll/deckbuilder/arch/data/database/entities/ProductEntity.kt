package com.r0adkll.deckbuilder.arch.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "marketplace_products")
class ProductEntity(
    @PrimaryKey var cardId: String,
    var setCode: String,
    var groupId: Long,
    var productId: Long,
    var productName: String,
    var url: String,
    var modifiedOn: Long
)
