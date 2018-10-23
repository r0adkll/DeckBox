package com.r0adkll.deckbuilder.arch.data.features.editing.mapping

import com.r0adkll.deckbuilder.arch.data.databasev2.entities.*
import com.r0adkll.deckbuilder.arch.data.databasev2.relations.SessionWithChanges
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Ability
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Change
import com.r0adkll.deckbuilder.arch.domain.features.editing.model.Session
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.util.*
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.Type


object RoomEntityMapper {

    fun createAddChange(sessionId: Long, card: PokemonCard, searchSessionId: String?): SessionChangeEntity {
        return SessionChangeEntity(0L, sessionId, card.id, 1, searchSessionId)
    }


    fun createRemoveChange(sessionId: Long, card: PokemonCard, searchSessionId: String?): SessionChangeEntity {
        return SessionChangeEntity(0L, sessionId, card.id, -1, searchSessionId)
    }


    fun to(entity: SessionWithChanges, cards: List<SessionCardEntity>, expansions: List<Expansion>): Session {
        return Session(
                entity.session.uid,
                entity.session.deckId,
                entity.session.name ?: "",
                entity.session.description ?: "",
                entity.session.image?.let { DeckImage.from(it) },
                from(expansions, cards).unstack(),
                calculateChanges(entity),
                entity.changes.map { to(it) }
        )
    }


    fun to(entity: SessionChangeEntity): Change {
        return Change(
                entity.uid,
                entity.cardId,
                entity.change,
                entity.searchSessionId
        )
    }


    fun to(card: PokemonCard): CardEntity {
        return CardEntity(
                card.id,
                card.name,
                card.number,
                card.text?.joinToString("\n"),
                card.artist,
                card.rarity,
                card.nationalPokedexNumber,
                card.hp,
                card.retreatCost?.size ?: 0,
                card.types?.compactTypes(),
                card.supertype.displayName,
                card.subtype.displayName,
                card.evolvesFrom,
                card.series,
                card.expansion?.name ?: "",
                card.expansion?.code ?: "",
                card.imageUrl,
                card.imageUrlHiRes,
                card.ability?.let { AbilityEntity(it.name, it.text) },
                card.weaknesses?.compactEffects(),
                card.resistances?.compactEffects()
        )
    }


    fun to(sessionId: Long, stack: StackedPokemonCard): SessionCardJoin {
        return SessionCardJoin(sessionId, stack.card.id, stack.count)
    }


    fun from(expansions: List<Expansion>, entities: List<SessionCardEntity>): List<StackedPokemonCard> {
        return entities.map { e ->
            StackedPokemonCard(
                    PokemonCard(
                            e.card.id,
                            e.card.name,
                            e.card.nationalPokedexNumber,
                            e.card.imageUrl,
                            e.card.imageUrlHiRes,
                            e.card.types?.deserializeTypes(),
                            SuperType.find(e.card.superType),
                            SubType.find(e.card.subType),
                            e.card.evolvesFrom,
                            e.card.hp,
                            (0 until e.card.retreatCost).map { Type.COLORLESS },
                            e.card.number,
                            e.card.artist,
                            e.card.rarity,
                            e.card.series,
                            expansions.find { it.code == e.card.setCode },
                            e.card.text?.split("\n"),
                            null,
                            e.card.weaknesses?.deserializeEffects(),
                            e.card.resistances?.deserializeEffects(),
                            e.card.ability?.let {
                                Ability(it.name, it.text)
                            }
                    ),
                    e.count
            )
        }
    }


    private fun calculateChanges(entity: SessionWithChanges): Boolean {
        var anyCardChange = false
        entity.changes.groupBy { it.cardId }.forEach { (_, changes) ->
            if (changes.sumBy { it.change } != 0) {
                anyCardChange = true
            }
        }
        return anyCardChange
                || entity.session.originalName != entity.session.name
                || entity.session.originalDescription != entity.session.description
                || entity.session.originalImage != entity.session.image
    }
}