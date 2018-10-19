package com.r0adkll.deckbuilder.arch.data.databasev2.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "cards")
class CardEntity(
        @PrimaryKey var id: String,
        var name: String,
        var number: String,
        var text: String?,
        var artist: String,
        var rarity: String?,
        var nationalPokedexNumber: Int?,
        var hp: Int?,
        var retreatCost: Int,
        var types: String?,
        var superType: String,
        var subType: String,
        var evolvesFrom: String?,
        var series: String,
        var expansionSet: String,
        var setCode: String,
        var imageUrl: String,
        var imageUrlHiRes: String,

        @Embedded var ability: AbilityEntity?,
        var weaknesses: String?,
        var resistances: String?
)