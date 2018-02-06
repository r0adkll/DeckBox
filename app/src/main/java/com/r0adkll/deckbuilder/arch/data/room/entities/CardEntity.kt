package com.r0adkll.deckbuilder.arch.data.room.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType


@Entity(tableName = "cards")
class CardEntity(
        @PrimaryKey(autoGenerate = true) var id: Long = 0,
        var cardId: String,
        var name: String,
        var nationalPokedexNumber: Int?,
        var imageUrl: String,
        var imageUrlHiRes: String,
        var types: String,
        var superType: String,
        var subType: String,
        var evolvesFrom: String?,
        var hp: Int?,
        var retreatCost: String?,
        var number: String,
        var artist: String,
        var rarity: String?,
        var series: String,
        var set: String,
        var setCode: String,
        var text: String
)