package com.r0adkll.deckbuilder.arch.data.mappings


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.*
import io.pokemontcg.model.Card


object CardMapper {

    fun to(card: Card, expansions: List<Expansion>): PokemonCard {
        return PokemonCard(
                card.id,
                card.name,
                card.nationalPokedexNumber,
                card.imageUrl,
                card.imageUrlHiRes,
                card.types,
                card.supertype,
                card.subtype,
                card.evolvesFrom,
                card.hp,
                card.retreatCost,
                card.number,
                card.artist,
                card.rarity,
                card.series,
                expansions.find { it.code == card.setCode },
                card.text,
                card.attacks?.map { to(it) },
                card.weaknesses?.map { to(it) },
                card.resistances?.map { to(it) },
                card.ability?.let { to(it) }
        )
    }


    fun to(atk: io.pokemontcg.model.Attack): Attack {
        return Attack(atk.cost, atk.name, atk.text, atk.damage, atk.convertedEnergyCost)
    }


    fun to(effect: io.pokemontcg.model.Effect): Effect {
        return Effect(effect.type, effect.value)
    }


    fun to(ability: io.pokemontcg.model.Ability): Ability {
        return Ability(ability.name, ability.text)
    }
}