package com.r0adkll.deckbuilder.arch.data.features.decks.mapper


import android.net.Uri
import android.util.ArrayMap
import com.r0adkll.deckbuilder.arch.data.features.decks.model.*
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.*
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.util.*
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.pokemontcg.model.Type


object EntityMapper {

    fun to(deck: Deck): DeckEntity {
        return DeckEntity("",
                deck.name,
                deck.description,
                deck.image?.uri?.toString(),
                /* Deprecated */ emptyList(),
                deck.cards.stack().map { to(it) },
                System.currentTimeMillis()
        )
    }


    fun to(entity: DeckEntity, cards: List<PokemonCard>, isMissingCards: Boolean): Deck {
        return Deck(
                entity.id,
                entity.name,
                entity.description,
                entity.image?.let { DeckImage.from(Uri.parse(it)) },
                cards,
                isMissingCards,
                entity.timestamp
        )
    }


    fun to(entity: DeckEntity, cards: List<PokemonCard>): Deck {
        var isMissingCards = false
        val stackedCards = ArrayList<StackedPokemonCard>()
        val metadata = entity.metadata()

        metadata.forEach { meta ->
            // find card
            val card = cards.find { it.id == meta.id }
            if (card != null) {
                stackedCards.add(StackedPokemonCard(card, meta.count))
            } else {
                isMissingCards = true
            }
        }

        return EntityMapper.to(entity, stackedCards.unstack(), isMissingCards)
    }


    fun to(stackedCard: StackedPokemonCard): CardMetadataEntity {
        return CardMetadataEntity(
                stackedCard.card.id,
                stackedCard.card.supertype.displayName,
                stackedCard.card.imageUrl,
                stackedCard.card.imageUrlHiRes,
                stackedCard.count
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


    fun to(entity: PokemonCardEntity, count: Int): CardMetadataEntity {
        return CardMetadataEntity(entity.id, entity.supertype, entity.imageUrl, entity.imageUrlHiRes, count)
    }


    fun DeckEntity.migrate(): DeckEntity {
        val metadata = this.metadata()
        return DeckEntity(this.id, this.name, this.description, this.image, emptyList(), metadata, this.timestamp)
    }


    fun DeckEntity.metadata(): List<CardMetadataEntity> {
        return this.cardMetadata
                ?: this.cards.stackCards().map {
                    EntityMapper.to(it.first, it.second)
                }
    }


    fun List<PokemonCardEntity>.stackCards(): List<Pair<PokemonCardEntity, Int>> {
        val map = ArrayMap<PokemonCardEntity, Int>(this.size)
        this.forEach { card ->
            val count = map[card] ?: 0
            map[card] = count + 1
        }
        return map.map { Pair(it.key, it.value) }
    }
}