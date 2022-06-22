package com.r0adkll.deckbuilder.arch.data.features.decks.model

import com.r0adkll.deckbuilder.arch.data.database.entities.AbilityEntity
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Ability

@Deprecated(
    message = "Moving away from storing full card object in Firestore",
    replaceWith = ReplaceWith("CardMetadataEntity", "com.deckbuilder.arch.data.features.decks.model.CardMetadataEntity")
)
class PokemonCardEntity(
    val id: String = "",
    val name: String = "",
    val nationalPokedexNumbers: List<Int>? = null,
    val imageUrl: String = "",
    val imageUrlHiRes: String = "",
    val types: String? = null,
    val supertype: String = "",
    val subtypes: List<String>? = null,
    val evolvesFrom: String? = null,
    val hp: Int? = null,
    val retreatCost: Int? = null,
    val number: String = "",
    val artist: String = "",
    val rarity: String? = null,
    val series: String = "",
    val expansionCode: String? = null,
//    val text: String? = null,
    val weaknesses: String? = null,
    val resistances: String? = null,
    val abilities: List<Ability>? = null,
)
