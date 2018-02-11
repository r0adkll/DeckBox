package com.r0adkll.deckbuilder.arch.domain.features.editing.model


import paperparcel.PaperParcel
import paperparcel.PaperParcelable


@PaperParcel
data class Session(
        val id: Long,
        val deckId: String?,
        val name: String?,
        val description: String?
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelSession.CREATOR
    }
}