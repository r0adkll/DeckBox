package com.r0adkll.deckbuilder.arch.data.room.entities


import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey


@Entity(
        tableName = "attacks",
        foreignKeys = [(ForeignKey(
                entity = CardEntity::class,
                parentColumns = [("cardId")],
                childColumns = [("cardId")],
                onDelete = CASCADE
        ))]
)
class AttackEntity(
        @PrimaryKey(autoGenerate = true) var id: Long = 0,
        var cardId: String,

        /*
         * Type Serialization:
         * "WF" = ["Water", "Fire"]
         */
        var cost: String?,
        var name: String,
        var text: String?,
        var damage: String?
)