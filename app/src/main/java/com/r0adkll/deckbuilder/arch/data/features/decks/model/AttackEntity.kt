package com.r0adkll.deckbuilder.arch.data.features.decks.model



class AttackEntity(
        val cost: String = "",
        val name: String = "",
        val text: String = "",
        val damage: String? = "",
        val convertedEnergyCost: Int = 0
)