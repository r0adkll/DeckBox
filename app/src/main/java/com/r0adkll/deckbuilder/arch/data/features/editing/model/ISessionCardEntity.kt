package com.r0adkll.deckbuilder.arch.data.features.editing.model

import io.requery.*


@Table(name = "session_cards")
@Entity
interface ISessionCardEntity : Persistable {

    @get:Key
    @get:Generated
    var id: Long

    @get:ManyToOne
    var session: ISessionEntity

    var cardId: String

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
}