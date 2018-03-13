package com.r0adkll.deckbuilder.arch.data.features.decks.model


class PokemonCardEntity(
        val id: String = "",
        val name: String = "",
        val nationalPokedexNumber: Int? = null,
        val imageUrl: String = "",
        val imageUrlHiRes: String = "",
        val types: String? = null,
        val supertype: String = "",
        val subtype: String = "",
        val evolvesFrom: String? = null,
        val hp: Int? = null,
        val retreatCost: Int? = null,
        val number: String = "",
        val artist: String = "",
        val rarity: String? = null,
        val series: String = "",
        val expansionCode: String? = null,
        val text: String? = null,
        val weaknesses: String? = null,
        val resistances: String? = null,
        val abilityName: String? = null,
        val abilityText: String? = null
)