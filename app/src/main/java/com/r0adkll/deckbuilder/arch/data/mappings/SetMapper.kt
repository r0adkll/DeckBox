package com.r0adkll.deckbuilder.arch.data.mappings

import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import io.pokemontcg.model.CardSet

object SetMapper {

    fun to(set: CardSet): Expansion {
        return Expansion(set.code, set.ptcgoCode, set.name, set.series, set.totalCards, set.standardLegal,
            set.expandedLegal, set.releaseDate, set.symbolUrl, set.logoUrl)
    }
}
