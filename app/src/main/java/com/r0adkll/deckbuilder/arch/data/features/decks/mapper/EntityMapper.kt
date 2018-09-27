package com.r0adkll.deckbuilder.arch.data.features.decks.mapper


import android.net.Uri
import com.r0adkll.deckbuilder.arch.data.features.decks.model.*
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.*
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.util.compactEffects
import com.r0adkll.deckbuilder.util.compactTypes
import com.r0adkll.deckbuilder.util.deserializeEffects
import com.r0adkll.deckbuilder.util.deserializeTypes
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.Type


object EntityMapper {

    fun to(deck: Deck): DeckEntity {
        return DeckEntity(
                deck.name,
                deck.description,
                deck.image?.uri?.toString(),
                deck.cards.map { to(it) },
                System.currentTimeMillis()
        )
    }


    fun to(expansions: List<Expansion>, entity: DeckEntity, id: String): Deck {
        return Deck(
                id,
                entity.name,
                entity.description,
                entity.image?.let { DeckImage.from(Uri.parse(it)) },
                entity.cards.map { to(it, expansions) },
                entity.timestamp
        )
    }


    fun to(card: PokemonCard): PokemonCardEntity {
        return PokemonCardEntity(
                card.id,
                card.name,
                card.nationalPokedexNumber,
                card.imageUrl,
                card.imageUrlHiRes,
                card.types?.compactTypes(),
                card.supertype.displayName,
                card.subtype.displayName,
                card.evolvesFrom,
                card.hp,
                card.retreatCost?.size,
                card.number,
                card.artist,
                card.rarity,
                card.series,
                card.expansion?.code,
                card.text?.joinToString("\n"),
//                card.attacks?.map { to(it) },
                card.weaknesses?.compactEffects(),
                card.resistances?.compactEffects(),
                card.ability?.name,
                card.ability?.text
        )
    }


    fun to(entity: PokemonCardEntity, expansions: List<Expansion>): PokemonCard {
        return PokemonCard(
                entity.id,
                entity.name,
                entity.nationalPokedexNumber,
                entity.imageUrl,
                entity.imageUrlHiRes,
                entity.types?.deserializeTypes(),
                SuperType.find(entity.supertype),
                SubType.find(entity.subtype),
                entity.evolvesFrom,
                entity.hp,
                entity.retreatCost?.let { (0 until it).map { _ -> Type.COLORLESS } },
                entity.number,
                entity.artist,
                entity.rarity,
                entity.series,
                entity.expansionCode?.let { code -> expansions.find { it.code == code } },
                entity.text?.split("\n"),
                null,
                entity.weaknesses?.deserializeEffects(),
                entity.resistances?.deserializeEffects(),
                entity.abilityName?.let { Ability(it, entity.abilityText ?: "") }
//                entity.attacks?.map { to(it) },
        )
    }


    fun to(expansion: Expansion): ExpansionEntity {
        return ExpansionEntity(
                expansion.code,
                expansion.ptcgoCode,
                expansion.name,
                expansion.series,
                expansion.totalCards,
                expansion.standardLegal,
                expansion.expandedLegal,
                expansion.releaseDate,
                expansion.symbolUrl,
                expansion.logoUrl
        )
    }


    fun to(entity: ExpansionEntity): Expansion {
        return Expansion(
                entity.code,
                entity.ptcgoCode,
                entity.name,
                entity.series,
                entity.totalCards,
                entity.standardLegal,
                entity.expandedLegal,
                entity.releaseDate,
                entity.symbolUrl,
                entity.logoUrl
        )
    }


    fun to(attack: Attack): AttackEntity {
        return AttackEntity(
//                attack.cost.map { it.displayName },
                attack.name,
                attack.text ?: "",
                attack.damage,
                attack.convertedEnergyCost
        )
    }


    fun to(entity: AttackEntity): Attack {
        return Attack(
                emptyList(),
                entity.name,
                entity.text,
                entity.damage,
                entity.convertedEnergyCost
        )
    }


    fun to(effect: Effect): EffectEntity {
        return EffectEntity(
                effect.type.displayName,
                effect.value
        )
    }


    fun to(entity: EffectEntity): Effect {
        return Effect(Type.find(entity.type), entity.value)
    }
}