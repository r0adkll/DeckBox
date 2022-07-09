package com.r0adkll.deckbuilder.arch.data.mappings

import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import io.pokemontcg.model.CardSet
import io.pokemontcg.model.Legality

object SetMapper {

  fun to(set: CardSet): Expansion {
    return Expansion(
      code = set.id,
      ptcgoCode = set.ptcgoCode,
      name = set.name,
      series = set.series,
      totalCards = set.printedTotal,
      standardLegal = set.legalities.standard == Legality.LEGAL,
      expandedLegal = set.legalities.expanded == Legality.LEGAL,
      releaseDate = set.releaseDate,
      symbolUrl = set.images.symbol,
      logoUrl = set.images.logo,
    )
  }
}
