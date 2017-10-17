package com.r0adkll.deckbuilder.arch.domain.features.cards.model


import io.pokemontcg.model.Type
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


@PaperParcel
data class Effect(
        val type: Type,
        val value: String
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelEffect.CREATOR
    }
}