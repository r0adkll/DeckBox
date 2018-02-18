package com.r0adkll.deckbuilder.arch.data.features.decks.mapper


import com.r0adkll.deckbuilder.arch.data.features.decks.model.*
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Attack
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Effect
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.Type


object EntityMapper {

    fun to(deck: Deck): DeckEntity {
        return DeckEntity(
                deck.name,
                deck.description,
                deck.cards.map { to(it) },
                System.currentTimeMillis()
        )
    }


    fun to(expansions: List<Expansion>, entity: DeckEntity, id: String): Deck {
        return Deck(
                id,
                entity.name,
                entity.description,
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
//                card.types?.map { it.displayName },
                card.supertype.displayName,
                card.subtype.displayName,
                card.evolvesFrom,
                card.hp,
//                card.retreatCost?.map { it.displayName },
                card.number,
                card.artist,
                card.rarity,
                card.series,
                card.expansion?.let { it.code }
//                card.text,
//                card.attacks?.map { to(it) },
//                card.weaknesses?.map { to(it) },
//                card.resistances?.map { to(it) }
        )
    }


    fun to(entity: PokemonCardEntity, expansions: List<Expansion>): PokemonCard {
        return PokemonCard(
                entity.id,
                entity.name,
                entity.nationalPokedexNumber,
                entity.imageUrl,
                entity.imageUrlHiRes,
//                entity.types?.map { Type.find(it) },
                null,
                SuperType.find(entity.supertype),
                SubType.find(entity.subtype),
                entity.evolvesFrom,
                entity.hp,
                null,
//                entity.retreatCost?.map { Type.find(it) },
                entity.number,
                entity.artist,
                entity.rarity,
                entity.series,
                entity.expansionCode?.let { code -> expansions.find { it.code == code } },
                null, null, null, null, null
//                entity.text,
//                entity.attacks?.map { to(it) },
//                entity.weaknesses?.map { to(it) },
//                entity.resistances?.map { to(it) }
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
                "" // FIXME: Replace this once SDK has been updated
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
                entity.symbolUrl
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