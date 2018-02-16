package com.r0adkll.deckbuilder.arch.data.features.editing.model

import io.requery.*


@Table(name = "session_changes")
@Entity
interface IChangeEntity : Persistable{

    @get:Key
    @get:Generated
    var id: Long

    @get:ManyToOne
    var session: ISessionEntity

    var cardId: String
    var change: Int // 1 - Add, -1 - Remove

    var searchSessionId: String?
}