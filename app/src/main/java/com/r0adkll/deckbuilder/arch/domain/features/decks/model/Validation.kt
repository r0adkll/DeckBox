package com.r0adkll.deckbuilder.arch.domain.features.decks.model


import paperparcel.PaperParcel
import paperparcel.PaperParcelable


@PaperParcel
data class Validation(
        val standard: Boolean,
        val expanded: Boolean
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelValidation.CREATOR
    }
}