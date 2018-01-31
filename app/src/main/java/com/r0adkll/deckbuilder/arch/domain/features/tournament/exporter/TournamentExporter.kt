package com.r0adkll.deckbuilder.arch.domain.features.tournament.exporter


import android.content.Context
import com.r0adkll.deckbuilder.arch.domain.features.decks.model.Deck
import com.r0adkll.deckbuilder.arch.domain.features.tournament.model.PlayerInfo
import io.reactivex.Observable
import java.io.File


interface TournamentExporter {

    fun export(activityContext: Context, deck: Deck, playerInfo: PlayerInfo): Observable<File>
}