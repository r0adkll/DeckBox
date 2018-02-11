package com.r0adkll.deckbuilder.arch.data.features.editing.model

import io.requery.*


@Table(name = "sessions")
@Entity
interface ISessionEntity : Persistable {

    @get:Key
    @get:Generated
    var id: Long

    var deckId: String?
    var name: String?
    var description: String?

    @get:OneToMany
    var cards: List<ISessionCardEntity>
}