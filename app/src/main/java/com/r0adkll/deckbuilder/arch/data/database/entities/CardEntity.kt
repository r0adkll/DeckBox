package com.r0adkll.deckbuilder.arch.data.database.entities

import androidx.room.*
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun nationalPokedexNumbersToJson(value: List<Int>?) = Gson().toJson(value)
    @TypeConverter
    fun subTypesToJson(value: List<String>?) = Gson().toJson(value)
    @TypeConverter
    fun abilitiesToJson(value: List<AbilityEntity>?) = Gson().toJson(value)

    @TypeConverter
    fun nationalPokedexNumbersFromJson(value: String) = Gson().fromJson(value, Array<Int>::class.java).toList()
    @TypeConverter
    fun subTypesFromJson(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()
    @TypeConverter
    fun abilitiesFromJson(value: String) = Gson().fromJson(value, Array<AbilityEntity>::class.java).toList()
}

@Entity(tableName = "cards")
@TypeConverters(Converters::class)
class CardEntity(
    @PrimaryKey var id: String,
    var name: String,
    var number: String,
    var text: String?,
    var artist: String,
    var rarity: String?,
    var nationalPokedexNumbers: List<Int>?,
    var hp: Int?,
    var retreatCost: Int,
    var types: String?,
    var superType: String,
    var subTypes: List<String>?,
    var evolvesFrom: String?,
    var series: String,
    var expansionSet: String,
    var setCode: String?,
    var imageUrl: String,
    var imageUrlHiRes: String,

    var abilities: List<AbilityEntity>?,
    var weaknesses: String?,
    var resistances: String?
)
