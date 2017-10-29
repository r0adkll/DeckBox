package com.r0adkll.deckbuilder.arch.data.features.decks.model


class PokemonCardEntity(
        val id: String = "",
        val name: String = "",
        val nationalPokedexNumber: Int? = null,
        val imageUrl: String = "",
        val imageUrlHiRes: String = "",
//        val types: List<String>? = null,
        val supertype: String = "",
        val subtype: String = "",
        val evolvesFrom: String? = null,
        val hp: Int? = null,
//        val retreatCost: List<String>? = null,
        val number: String = "",
        val artist: String = "",
        val rarity: String? = null,
        val series: String = "",
        val expansion: ExpansionEntity? = null
//        val text: List<String>? = null,
//        val attacks: List<AttackEntity>? = null,
//        val weaknesses: List<EffectEntity>? = null,
//        val resistances: List<EffectEntity>? = null
)