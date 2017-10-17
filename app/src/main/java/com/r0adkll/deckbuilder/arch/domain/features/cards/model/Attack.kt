package com.r0adkll.deckbuilder.arch.domain.features.cards.model


import io.pokemontcg.model.Type
import paperparcel.PaperParcel
import paperparcel.PaperParcelable


@PaperParcel
data class Attack(
        val cost: List<Type>,
        val name: String,
        val text: String,
        val damage: String,
        val convertedEnergyCost: Int
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelAttack.CREATOR
    }
}