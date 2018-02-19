package com.r0adkll.deckbuilder.arch.data.features.editing.mapping

import com.r0adkll.deckbuilder.arch.data.features.editing.model.*
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Ability
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Effect
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Change
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Session
import com.r0adkll.deckbuilder.util.compact
import com.r0adkll.deckbuilder.util.type
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.Type


object EntityMapper {

    fun createAddChange(card: PokemonCard, searchSessionId: String?): ChangeEntity {
        val e = ChangeEntity()
        e.cardId = card.id
        e.searchSessionId = searchSessionId
        e.change = 1
        return e
    }


    fun createRemoveChange(card: PokemonCard, searchSessionId: String?): ChangeEntity {
        val e = ChangeEntity()
        e.cardId = card.id
        e.searchSessionId = searchSessionId
        e.change = -1
        return e
    }


    fun to(entity: SessionEntity, expansions: List<Expansion>): Session {
        return Session(
                entity.id,
                entity.deckId,
                entity.name ?: "",
                entity.description ?: "",
                from(expansions, entity.cards),
                calculateChanges(entity),
                entity.changes.map { to(it) }
        )
    }


    fun to(entity: IChangeEntity): Change {
        return Change(
                entity.id,
                entity.cardId,
                entity.change,
                entity.searchSessionId
        )
    }


    fun to(card: PokemonCard): SessionCardEntity {
        val entity = SessionCardEntity()
        entity.cardId = card.id
        entity.name = card.name
        entity.nationalPokedexNumber = card.nationalPokedexNumber
        entity.imageUrl = card.imageUrl
        entity.imageUrlHiRes = card.imageUrlHiRes
        entity.types = card.types?.compactTypes()
        entity.superType = card.supertype.displayName
        entity.subType = card.subtype.displayName
        entity.evolvesFrom = card.evolvesFrom
        entity.hp = card.hp
        entity.retreatCost = card.retreatCost?.size ?: 0
        entity.number = card.number
        entity.artist = card.artist
        entity.rarity = card.rarity
        entity.series = card.series
        entity.expansionSet = card.expansion?.name
        entity.setCode = card.expansion?.code
        entity.text = card.text?.joinToString("\n")
        entity.abilityName = card.ability?.name
        entity.abilityText = card.ability?.text
        entity.weaknesses = card.weaknesses?.compactEffects()
        entity.resistances = card.resistances?.compactEffects()
        return entity
    }


    fun from(expansions: List<Expansion>, entities: List<ISessionCardEntity>): List<PokemonCard> {
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
                    null,
                    e.weaknesses?.deserializeEffects(),
                    e.resistances?.deserializeEffects(),
                    e.abilityName?.let {
                        Ability(it, e.abilityText!!)
                    }
            )
        }
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


    private fun calculateChanges(entity: SessionEntity): Boolean {
        var anyCardChange = false
        entity.changes.groupBy { it.cardId }.forEach { (_, changes) ->
            if (changes.sumBy { it.change } != 0) {
                anyCardChange = true
            }
        }
        return anyCardChange || entity.originalName != entity.name || entity.originalDescription != entity.description
    }
}