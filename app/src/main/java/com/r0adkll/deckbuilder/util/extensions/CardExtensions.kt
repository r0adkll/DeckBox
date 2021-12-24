package com.r0adkll.deckbuilder.util.extensions

import com.r0adkll.deckbuilder.arch.domain.SubTypes
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import io.pokemontcg.model.SuperType

fun Iterable<PokemonCard>.isMulligan(): Boolean {
    return this.none {
        it.supertype == SuperType.POKEMON &&
            (it.subtype == SubTypes.BASIC || it.evolvesFrom.isNullOrBlank())
    }
}

internal val sortableNumberRegex by lazy {
    "\\d+".toRegex()
}
/**
 * Get a sortable number
 */
val PokemonCard.sortableNumber: Int
    get() = sortableNumberRegex.find(number)?.value?.toIntOrNull()
        ?: number.replace("a", "", true)
            .replace("sm", "", true)
            .replace("sv", "", true)
            .replace("rc", "", true)
            .toIntOrNull() ?: -1
