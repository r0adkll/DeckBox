package com.r0adkll.deckbuilder.arch.data.features.editing.mapping

import com.r0adkll.deckbuilder.arch.data.features.editing.model.*
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Ability
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Change
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Session
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.util.compactEffects
import com.r0adkll.deckbuilder.util.compactTypes
import com.r0adkll.deckbuilder.util.deserializeEffects
import com.r0adkll.deckbuilder.util.deserializeTypes
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
                entity.image?.let { DeckImage.from(it) },
                from(expansions, entity.cards.toList()),
                calculateChanges(entity),
                entity.changes.toList().map { to(it) }
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


    private fun calculateChanges(entity: SessionEntity): Boolean {
        var anyCardChange = false
        entity.changes.toList().groupBy { it.cardId }.forEach { (_, changes) ->
            if (changes.sumBy { it.change } != 0) {
                anyCardChange = true
            }
        }
        return anyCardChange
                || entity.originalName != entity.name
                || entity.originalDescription != entity.description
                || entity.originalImage != entity.image
    }
}