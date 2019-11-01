package com.r0adkll.deckbuilder.arch.domain.features.editing.model

import android.os.Parcelable
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Session(
    val id: Long,
    val deckId: String?,
    val name: String,
    val description: String,
    val image: DeckImage?,
    val collectionOnly: Boolean,
    val cards: List<PokemonCard>,
    val hasChanges: Boolean,
    val changes: List<Change>
) : Parcelable {

    override fun toString(): String {
        return "Session(id=$id, deckId=$deckId, name='$name', description='$description', " +
            "image=$image, collectionOnly=$collectionOnly, cards=${cards.size}, " +
            "hasChanges=$hasChanges, changes=${changes.size})"
    }

    companion object {

        const val NO_ID = -1L
    }
}
