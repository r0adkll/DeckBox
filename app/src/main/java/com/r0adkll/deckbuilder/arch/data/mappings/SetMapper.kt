package com.r0adkll.deckbuilder.arch.data.mappings

import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import io.pokemontcg.model.CardSet
import io.pokemontcg.model.Legality

object SetMapper {

    fun to(set: CardSet): Expansion {
        return Expansion(set.id, set.ptcgoCode, set.name, set.series, set.total, set.legalities.standard == Legality.LEGAL,
            set.legalities.expanded == Legality.LEGAL, set.releaseDate, set.images.symbol, set.images.logo)
    }
}
