package com.r0adkll.deckbuilder.tools

import com.r0adkll.deckbuilder.arch.domain.CardSet
import com.r0adkll.deckbuilder.arch.domain.PokemonCard
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType


object ModelUtils {

    fun createPokemonCard(): PokemonCard {
        return PokemonCard("", "", null, "", "", null, SuperType.POKEMON, SubType.BASIC, null, null,
                null, ",", "", "", "", CardSet("", "", "", "", 0, false, false, "", ""), null, null,
                null, null)
    }
}