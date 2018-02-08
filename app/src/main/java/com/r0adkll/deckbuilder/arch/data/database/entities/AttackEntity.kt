package com.r0adkll.deckbuilder.arch.data.database.entities


import io.requery.*


@Table(name = "attacks")
@Entity
interface IAttackEntity : Persistable {

    @get:Key
    @get:Generated
    var id: Long

    @get:ManyToOne
    var card: ICardEntity

    var cost: String?
    var name: String
    var text: String?
    var damage: String?
}