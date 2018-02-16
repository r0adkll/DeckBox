package com.r0adkll.deckbuilder.arch.domain.features.editing.model


import paperparcel.PaperParcel
import paperparcel.PaperParcelable


@PaperParcel
data class Change(
        val id: Long,
        val cardId: String,
        val change: Int,
        val searchSessionId: String?
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelChange.CREATOR
    }
}