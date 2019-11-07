package com.r0adkll.deckbuilder.arch.domain.features.importer.repository

import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import io.reactivex.Observable

interface Importer {

    /**
     * Import a deck list string
     * @param deckList The decklist formatted string to import
     * @return an Observable that will pipe out the list of [PokemonCard] objects for the decklist
     */
    fun import(deckList: String): Observable<List<PokemonCard>>
}
