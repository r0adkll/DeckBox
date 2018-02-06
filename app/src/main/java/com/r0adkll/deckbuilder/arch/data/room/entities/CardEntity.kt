package com.r0adkll.deckbuilder.arch.data.room.entities


import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey


@Entity(
        tableName = "cards",
        indices = [
                Index("cardId", unique = true),
                Index(
                        "name",
                        "types",
                        "superType",
                        "subType",
                        "hp",
                        "retreatCost",
                        "number",
                        "rarity",
                        "setCode",
                        "text",
                        "abilityName",
                        "abilityText"
                )
        ]
)
class CardEntity(
        @PrimaryKey(autoGenerate = true) var id: Long = 0,
        var cardId: String,
        var name: String,
        var nationalPokedexNumber: Int?,
        var imageUrl: String,
        var imageUrlHiRes: String,

        /*
         * Type Serialization:
         * "WF" = ["Water", "Fire"]
         */
        var types: String?,
        var superType: String,
        var subType: String,
        var evolvesFrom: String?,
        var hp: Int?,

        /*
         * Type Serialization
         * "CC" -> ["Colorless", "Colorless"]
         */
        var retreatCost: String?,
        var number: String,
        var artist: String,
        var rarity: String?,
        var series: String,
        var set: String,
        var setCode: String,
        var text: String?,

        var abilityName: String?,
        var abilityText: String?,

        /*
         * Effect Serialization
         * "[F|×2][W|×2]"
         */
        var weaknesses: String?,

        /*
         * Effect Serialization
         * "[F|-20][D|-30]"
         */
        var resistances: String?
)