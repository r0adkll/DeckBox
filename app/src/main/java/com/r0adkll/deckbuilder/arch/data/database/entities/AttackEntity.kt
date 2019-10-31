package com.r0adkll.deckbuilder.arch.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
        tableName = "attacks",
        indices = [Index("cardId")],
        foreignKeys = [ForeignKey(
                entity = CardEntity::class,
                parentColumns = ["id"],
                childColumns = ["cardId"],
                onDelete = ForeignKey.CASCADE
        )]
)
class AttackEntity(
        @PrimaryKey(autoGenerate = true) var uid: Long,
        var cardId: String,

        var cost: String?,
        var convertedEnergyCost: Int,
        var name: String,
        var text: String?,
        var damage: String?,
        var convertedDamageCost: Int?
 )
