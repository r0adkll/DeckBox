package com.r0adkll.deckbuilder.arch.data.features.editing.model

import android.net.Uri
import io.requery.*


@Table(name = "sessions")
@Entity
interface ISessionEntity : Persistable {

    @get:Key
    @get:Generated
    var id: Long

    @get:Column(unique = true)
    var deckId: String?

    var originalName: String
    var originalDescription: String
    var originalImage: Uri?

    var name: String?
    var description: String?
    var image: Uri?

    @get:OneToMany
    var cards: List<ISessionCardEntity>

    @get:OneToMany
    var changes: List<IChangeEntity>
}