package com.r0adkll.deckbuilder.arch.domain.features.exporter.ptcgo

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import io.reactivex.Observable

interface PtcgoExporter {

    fun export(cards: List<PokemonCard>, name: String): Observable<String>
}
