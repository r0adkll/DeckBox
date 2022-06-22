package com.r0adkll.deckbuilder.arch.data.database.mapping

import com.r0adkll.deckbuilder.arch.data.database.entities.AbilityEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.AttackEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.CardEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.DeckEntity
import com.r0adkll.deckbuilder.arch.data.database.relations.CardWithAttacks
import com.r0adkll.deckbuilder.arch.data.database.relations.DeckStackedCard
import com.r0adkll.deckbuilder.arch.data.database.relations.StackedCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Ability
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Attack
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.util.compactCardEffects
import com.r0adkll.deckbuilder.util.compactEffects
import com.r0adkll.deckbuilder.util.compactTypes
import com.r0adkll.deckbuilder.util.deserializeEffects
import com.r0adkll.deckbuilder.util.deserializeTypes
import com.r0adkll.deckbuilder.util.unstack
import io.pokemontcg.model.Card
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.Type

object RoomEntityMapper {

    fun to(entity: DeckEntity, cards: List<StackedCard>, expansions: List<Expansion>): Deck {
        return Deck(
            entity.uid.toString(),
            entity.name,
            entity.description ?: "",
            entity.image?.let { DeckImage.from(it) },
            entity.collectionOnly,
            from(expansions, cards).unstack(),
            false,
            entity.timestamp
        )
    }

    fun toDeck(entity: DeckEntity, cards: List<DeckStackedCard>, expansions: List<Expansion>): Deck {
        return Deck(
            entity.uid.toString(),
            entity.name,
            entity.description ?: "",
            entity.image?.let { DeckImage.from(it) },
            entity.collectionOnly,
            fromDeck(expansions, cards).unstack(),
            false,
            entity.timestamp
        )
    }

    fun to(card: PokemonCard): CardWithAttacks {
        val cardEntity = CardEntity(
            card.id,
            card.name,
            card.number,
            null,
            card.artist,
            card.rarity,
            card.nationalPokedexNumbers,
            card.hp,
            card.retreatCost?.size ?: 0,
            card.types?.compactTypes(),
            card.supertype.displayName,
            card.subtypes,
            card.evolvesFrom,
            card.series,
            card.expansion?.name ?: "",
            card.expansion?.code ?: "",
            card.imageUrl,
            card.imageUrlHiRes,
            card.abilities?.map { AbilityEntity(it.name, it.text) },
            card.weaknesses?.compactEffects(),
            card.resistances?.compactEffects()
        )
        val attacks = card.attacks?.map {
            to(card.id, it)
        } ?: emptyList()
        return CardWithAttacks(cardEntity, attacks)
    }

    fun to(cardId: String, attack: Attack): AttackEntity {
        return AttackEntity(
            0L,
            cardId,
            attack.cost?.compactTypes(),
            attack.convertedEnergyCost,
            attack.name,
            attack.text,
            attack.damage,
            attack.damage?.replace("+", "")
                ?.replace("×", "")
                ?.replace("-", "")
                ?.toIntOrNull() ?: 0
        )
    }

    fun to(card: Card): CardWithAttacks {
        val cardEntity = CardEntity(
            card.id,
            card.name,
            card.number,
        null,
            card.artist ?: "",
            card.rarity,
            card.nationalPokedexNumbers,
            card.hp,
            card.retreatCost?.size ?: 0,
            card.types?.compactTypes(),
            card.supertype.displayName,
            card.subtypes,
            card.evolvesFrom,
            card.set.series,
            card.set.name,
            card.set.id,
            card.images.small,
            card.images.large,
            card.abilities?.map { AbilityEntity(it.name, it.text) },
            card.weaknesses?.compactCardEffects(),
            card.resistances?.compactCardEffects()
        )
        val attacks = card.attacks?.map {
            to(card.id, it)
        } ?: emptyList()
        return CardWithAttacks(cardEntity, attacks)
    }

    fun to(cardId: String, attack: io.pokemontcg.model.Attack): AttackEntity {
        return AttackEntity(
            0L,
            cardId,
            attack.cost?.compactTypes(),
            attack.convertedEnergyCost,
            attack.name,
            attack.text,
            attack.damage,
            attack.damage?.replace("+", "")
                ?.replace("×", "")
                ?.replace("-", "")
                ?.toIntOrNull() ?: 0
        )
    }

    fun from(expansions: List<Expansion>, entities: List<StackedCard>): List<StackedPokemonCard> {
        return entities.map { e ->
            StackedPokemonCard(
                PokemonCard(
                    e.card.card.id,
                    e.card.card.name,
                    e.card.card.nationalPokedexNumbers,
                    e.card.card.imageUrl,
                    e.card.card.imageUrlHiRes,
                    e.card.card.types?.deserializeTypes(),
                    SuperType.find(e.card.card.superType),
                    e.card.card.subTypes,
                    e.card.card.evolvesFrom,
                    e.card.card.hp,
                    (0 until e.card.card.retreatCost).map { Type.COLORLESS },
                    e.card.card.number,
                    e.card.card.artist,
                    e.card.card.rarity,
                    e.card.card.series,
                    expansions.find { it.code == e.card.card.setCode },
//                    e.card.card.text?.split("\n"),
                    e.card.attacks.map { from(it) },
                    e.card.card.weaknesses?.deserializeEffects(),
                    e.card.card.resistances?.deserializeEffects(),
                    e.card.card.abilities?.map {
                        Ability(it.name, it.text)
                    }
                ),
                e.count
            )
        }
    }

    fun fromDeck(expansions: List<Expansion>, entities: List<DeckStackedCard>): List<StackedPokemonCard> {
        return entities.map { e ->
            StackedPokemonCard(
                PokemonCard(
                    e.card.card.id,
                    e.card.card.name,
                    e.card.card.nationalPokedexNumbers,
                    e.card.card.imageUrl,
                    e.card.card.imageUrlHiRes,
                    e.card.card.types?.deserializeTypes(),
                    SuperType.find(e.card.card.superType),
                    e.card.card.subTypes,
                    e.card.card.evolvesFrom,
                    e.card.card.hp,
                    (0 until e.card.card.retreatCost).map { Type.COLORLESS },
                    e.card.card.number,
                    e.card.card.artist,
                    e.card.card.rarity,
                    e.card.card.series,
                    expansions.find { it.code == e.card.card.setCode },
//                    e.card.card.text?.split("\n"),
                    e.card.attacks.map { from(it) },
                    e.card.card.weaknesses?.deserializeEffects(),
                    e.card.card.resistances?.deserializeEffects(),
                    e.card.card.abilities?.map {
                        Ability(it.name, it.text)
                    }
                ),
                e.count
            )
        }
    }

    fun fromCards(expansions: List<Expansion>, entities: List<CardWithAttacks>): List<PokemonCard> {
        return entities.map { e ->
            PokemonCard(
                e.card.id,
                e.card.name,
                e.card.nationalPokedexNumbers,
                e.card.imageUrl,
                e.card.imageUrlHiRes,
                e.card.types?.deserializeTypes(),
                SuperType.find(e.card.superType),
                e.card.subTypes,
                e.card.evolvesFrom,
                e.card.hp,
                (0 until e.card.retreatCost).map { Type.COLORLESS },
                e.card.number,
                e.card.artist,
                e.card.rarity,
                e.card.series,
                expansions.find { it.code == e.card.setCode },
//                e.card.text?.split("\n"),
                e.attacks.map { from(it) },
                e.card.weaknesses?.deserializeEffects(),
                e.card.resistances?.deserializeEffects(),
                e.card.abilities?.map {
                    Ability(it.name, it.text)
                }
            )
        }
    }

    fun from(entity: AttackEntity): Attack {
        return Attack(
            entity.cost?.deserializeTypes(),
            entity.name,
            entity.text,
            entity.damage,
            entity.convertedEnergyCost
        )
    }
}
