package com.r0adkll.deckbuilder.arch.data.features.community.mapper

import android.net.Uri
import com.r0adkll.deckbuilder.arch.data.features.community.model.DeckInfoEntity
import com.r0adkll.deckbuilder.arch.data.features.community.model.ThemeDeckTemplateEntity
import com.r0adkll.deckbuilder.arch.data.features.community.model.TournamentDeckTemplateEntity
import com.r0adkll.deckbuilder.arch.data.features.community.model.TournamentEntity
import com.r0adkll.deckbuilder.arch.domain.Format
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.StackedPokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.community.model.DeckInfo
import com.r0adkll.deckbuilder.arch.domain.features.community.model.DeckTemplate
import com.r0adkll.deckbuilder.arch.domain.features.community.model.Tournament
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.deckimage.adapter.DeckImage
import com.r0adkll.deckbuilder.util.unstack

object EntityMapper {

    fun to(entity: TournamentDeckTemplateEntity, cards: List<PokemonCard>): DeckTemplate {
        var isMissingCards = false
        val stackedCards = ArrayList<StackedPokemonCard>()
        val metadata = entity.cardMetadata!!

        metadata.forEach { meta ->
            // find card
            val card = cards.find { it.id == meta.id }
            if (card != null) {
                stackedCards.add(StackedPokemonCard(card, meta.count))
            } else {
                isMissingCards = true
            }
        }

        val name = "#${entity.rank} ${entity.author}"
        val description = entity.tournament!!.name
        val deck = to(entity, stackedCards.unstack(), isMissingCards)
        return DeckTemplate.TournamentDeckTemplate(deck, name, description, entity.author,
            entity.authorCountry, to(entity.tournament), to(entity.deckInfo))
    }

    fun to(entity: ThemeDeckTemplateEntity, cards: List<PokemonCard>): DeckTemplate {
        var isMissingCards = false
        val stackedCards = ArrayList<StackedPokemonCard>()
        val metadata = entity.cardMetadata!!
        val expansion = cards.first().expansion!!

        metadata.forEach { meta ->
            // find card
            val card = cards.find { it.id == meta.id }
            if (card != null) {
                stackedCards.add(StackedPokemonCard(card, meta.count))
            } else {
                isMissingCards = true
            }
        }

        val deck = to(entity, stackedCards.unstack(), isMissingCards)
        return DeckTemplate.ThemeDeckTemplate(deck, entity.name, entity.description, expansion)
    }

    fun to(entity: TournamentDeckTemplateEntity, cards: List<PokemonCard>, isMissingCards: Boolean): Deck {
        val deckName = entity.deckInfo.joinToString(" ") { it.name.capitalize() }
        val deckDescription = "#${entity.rank} ${entity.author} of ${entity.tournament!!.name}"
        val deckImageCard = entity.deckInfo.map { info ->
            cards.find { it.name.contains(info.name, true) }
        }.firstOrNull { it != null }
        val deckImage = deckImageCard?.let { DeckImage.Pokemon(it.imageUrl) }
            ?: entity.image?.let { DeckImage.from(Uri.parse(it)) }

        return Deck(
            entity.id,
            deckName,
            deckDescription,
            deckImage,
            false,
            cards,
            isMissingCards,
            entity.timestamp
        )
    }

    fun to(entity: TournamentEntity): Tournament {
        return Tournament(
            entity.name,
            entity.date,
            entity.country,
            Format.of(entity.format),
            entity.playerCount
        )
    }

    fun to(entities: List<DeckInfoEntity>): List<DeckInfo> = entities.map {
        DeckInfo(it.name, it.iconUrl)
    }

    fun to(entity: ThemeDeckTemplateEntity, cards: List<PokemonCard>, isMissingCards: Boolean): Deck {
        return Deck(
            entity.id,
            entity.name,
            entity.description,
            entity.image?.let { DeckImage.from(Uri.parse(it)) },
            false,
            cards,
            isMissingCards,
            0L
        )
    }
}
