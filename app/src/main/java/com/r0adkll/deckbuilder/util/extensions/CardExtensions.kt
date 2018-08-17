package com.r0adkll.deckbuilder.util.extensions

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType


fun Iterable<PokemonCard>.isMulligan(): Boolean {
    return this.none {
        it.supertype == SuperType.POKEMON
                && (it.subtype == SubType.BASIC || it.evolvesFrom.isNullOrBlank())
    }
}