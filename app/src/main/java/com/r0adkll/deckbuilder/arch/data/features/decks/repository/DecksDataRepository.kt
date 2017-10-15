package com.r0adkll.deckbuilder.arch.data.features.decks.repository

import com.r0adkll.deckbuilder.arch.domain.CardSet
import com.r0adkll.deckbuilder.arch.domain.PokemonCard
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.decks.repository.DecksRepository
import io.pokemontcg.model.SubType
import io.pokemontcg.model.SuperType
import io.reactivex.Observable
import javax.inject.Inject


class DecksDataRepository @Inject constructor(

) : DecksRepository {

    override fun getDecks(): Observable<List<Deck>> {
        return Observable.just(emptyList()
//                listOf(
//                        Deck(0, "Roaring Fire", "", listOf(
//                                PokemonCard("sm1-001", "Incineroar", 0, "https://images.pokemontcg.io/smp/SM38_hires.png",
//                                        "https://images.pokemontcg.io/smp/SM38_hires.png", null, SuperType.POKEMON, SubType.GX, "Torracat", 160, null, "5", "", "", "",
//                                        CardSet("", "", "", "", 100, false, false, "", ""),
//                                        null, null, null, null)
//                        ))
//                )
        )
    }


    override fun deleteDeck(deck: Deck): Observable<List<Deck>> {
        return Observable.empty() // FIXME: Implement this
    }
}