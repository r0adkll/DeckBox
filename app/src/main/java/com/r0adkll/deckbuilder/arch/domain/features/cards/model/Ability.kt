package com.r0adkll.deckbuilder.arch.domain.features.cards.model

import paperparcel.PaperParcel
import paperparcel.PaperParcelable


@PaperParcel
data class Ability(
        val name: String,
        val text: String
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelAbility.CREATOR
    }
}