package com.r0adkll.deckbuilder.arch.data.database.entities


import io.requery.*


@Table(name = "cards")
@Entity
interface ICardEntity : Persistable {

    @get:Key
    @get:Generated
    var id: Long

    var cardId: String

    @get:Index(value = ["name_index"])
    var name: String
    var nationalPokedexNumber: Int?
    var imageUrl: String
    var imageUrlHiRes: String

    /*
     * Type Serialization:
     * "WF" = ["Water", "Fire"]
     */
    var types: String?
    var superType: String
    var subType: String
    var evolvesFrom: String?
    var hp: Int?

    /*
     * Type Serialization
     * "CC" -> ["Colorless", "Colorless"]
     */
    var retreatCost: Int
    var number: String
    var artist: String
    var rarity: String?
    var series: String
    var expansionSet: String
    var setCode: String
    var text: String?

    var abilityName: String?
    var abilityText: String?

    /*
     * Effect Serialization
     * "[F|×2][W|×2]"
     */
    var weaknesses: String?

    /*
     * Effect Serialization
     * "[F|-20][D|-30]"
     */
    var resistances: String?


    @get:OneToMany
    var attacks: List<IAttackEntity>
}