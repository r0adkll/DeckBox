package com.r0adkll.deckbuilder.arch.data.database.mapping

import com.r0adkll.deckbuilder.arch.data.database.entities.AttackEntity
import com.r0adkll.deckbuilder.arch.data.database.entities.CardEntity
import io.pokemontcg.model.Card
import io.pokemontcg.model.Effect
import io.pokemontcg.model.Type


object EntityMapper {

    fun to(models: List<Card>): Pair<List<CardEntity>, List<AttackEntity>> {
        val cards = ArrayList<CardEntity>()
        val attacks = ArrayList<AttackEntity>()

//        models.forEach { model ->
//            cards += CardEntity(
//                    cardId = model.id,
//                    name = model.name,
//                    nationalPokedexNumber = model.nationalPokedexNumber,
//                    imageUrl = model.imageUrl,
//                    imageUrlHiRes = model.imageUrlHiRes,
//                    types = model.types?.let { fromTypes(it) },
//                    superType = model.supertype.displayName,
//                    subType = model.subtype.displayName,
//                    evolvesFrom = model.evolvesFrom,
//                    hp = model.hp,
//                    retreatCost = model.retreatCost?.let { fromTypes(it) },
//                    number = model.number,
//                    artist = model.artist,
//                    rarity = model.rarity,
//                    series = model.series,
//                    set = model.set,
//                    setCode = model.setCode,
//                    text = model.text?.joinToString("\n"),
//                    abilityName = model.ability?.name,
//                    abilityText = model.ability?.text,
//                    weaknesses = model.weaknesses?.let { fromEffects(it) },
//                    resistances = model.resistances?.let { fromEffects(it) }
//            )
//
//            model.attacks?.forEach {
//                attacks += AttackEntity(
//                        cardId = model.id,
//                        cost = it.cost?.let { fromTypes(it) },
//                        name = it.name,
//                        text = it.text,
//                        damage = it.damage
//                )
//            }
//        }

        return Pair(cards, attacks)
    }


    fun fromTypes(types: List<Type>): String {
        return types.fold("", { acc, type ->
            acc.plus(type.displayName[0].toUpperCase())
        })
    }


    fun fromEffects(effects: List<Effect>): String {
        return effects.foldIndexed("", { index, acc, effect ->
            val new = acc.plus("[${effect.type.displayName[0].toUpperCase()}|${effect.value}]")
            if (index != effects.size-1) {
                new.plus(",")
            } else {
                new
            }
        })
    }
}