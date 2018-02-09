package com.r0adkll.deckbuilder.arch.data.database.mapping

import com.r0adkll.deckbuilder.arch.data.database.entities.AttackEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.CardEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.IAttackEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.ICardEntity
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Ability
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.util.compact
import com.r0adkll.deckbuilder.util.type
import io.pokemontcg.model.*


object EntityMapper {

    fun to(models: List<Card>): List<CardEntity> {
        return models.map { model ->
            val entity = CardEntity()

            entity.cardId = model.id
            entity.name = model.name
            entity.nationalPokedexNumber = model.nationalPokedexNumber
            entity.imageUrl = model.imageUrl
            entity.imageUrlHiRes = model.imageUrlHiRes
            entity.types = model.types?.compactTypes()
            entity.superType = model.supertype.displayName
            entity.subType = model.subtype.displayName
            entity.evolvesFrom = model.evolvesFrom
            entity.hp = model.hp
            entity.retreatCost = model.retreatCost?.size ?: 0
            entity.number = model.number
            entity.artist = model.artist
            entity.rarity = model.rarity
            entity.series = model.series
            entity.set = model.set
            entity.setCode = model.setCode
            entity.text = model.text?.joinToString("\n")
            entity.abilityName = model.ability?.name
            entity.abilityText = model.ability?.text
            entity.weaknesses = model.weaknesses?.compactEffects()
            entity.resistances = model.resistances?.compactEffects()
            entity.attacks = model.attacks?.map { to(entity, it) }

            entity
        }
    }


    fun to(card: CardEntity, attack: Attack): AttackEntity {
        val entity = AttackEntity()
        entity.card = card
        entity.cost = attack.cost?.compactTypes()
        entity.convertedEnergyCost = attack.cost?.size ?: 0
        entity.name = attack.name
        entity.text = attack.text
        entity.damage = attack.damage

        // Attempt to reduce down an attacks damage
        entity.convertedEnergyCost = attack.damage
                ?.replace("+", "")
                ?.replace("×", "")
                ?.replace("-", "")
                ?.toIntOrNull() ?: 0

        return entity
    }


    fun from(expansions: List<Expansion>, entities: List<ICardEntity>): List<PokemonCard> {
        return entities.map { e ->
            PokemonCard(
                    e.cardId,
                    e.name,
                    e.nationalPokedexNumber,
                    e.imageUrl,
                    e.imageUrlHiRes,
                    e.types?.deserializeTypes(),
                    SuperType.find(e.superType),
                    SubType.find(e.subType),
                    e.evolvesFrom,
                    e.hp,
                    (0 until e.retreatCost).map { Type.COLORLESS },
                    e.number,
                    e.artist,
                    e.rarity,
                    e.series,
                    expansions.find { it.code == e.setCode },
                    e.text?.split("\n"),
                    e.attacks.map { from(it) },
                    e.weaknesses?.deserializeEffects(),
                    e.resistances?.deserializeEffects(),
                    e.abilityName?.let {
                        Ability(it, e.abilityText!!)
                    }
            )
        }
    }


    fun from(entity: IAttackEntity): com.r0adkll.deckbuilder.arch.domain.features.cards.model.Attack {
        return com.r0adkll.deckbuilder.arch.domain.features.cards.model.Attack(
                entity.cost?.deserializeTypes(),
                entity.name,
                entity.text,
                entity.damage,
                entity.convertedDamageAmount
        )
    }


    private fun String.deserializeTypes(): List<Type> {
        return this.map {
            it.toString().type()
        }
    }


    private fun List<Type>.compactTypes(): String {
        return this.fold("", { acc, type ->
            acc.plus(type.compact())
        })
    }


    private fun String.deserializeEffects(): List<com.r0adkll.deckbuilder.arch.domain.features.cards.model.Effect> {
        return this.split(",").map {
            val parts = it.replace("[", "").replace("]", "").split("|")
            com.r0adkll.deckbuilder.arch.domain.features.cards.model.Effect(parts[0].type(), parts[1])
        }
    }


    private fun List<Effect>.compactEffects(): String {
        return this.foldIndexed("", { index, acc, effect ->
            val new = acc.plus("[${effect.type.displayName[0].toUpperCase()}|${effect.value}]")
            if (index != this.size - 1) {
                new.plus(",")
            } else {
                new
            }
        })
    }
}