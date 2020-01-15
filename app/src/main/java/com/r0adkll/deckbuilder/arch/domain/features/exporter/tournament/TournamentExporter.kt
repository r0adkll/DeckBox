package com.r0adkll.deckbuilder.arch.domain.features.exporter.tournament

import android.content.Context
import com.r0adkll.deckbuilder.arch.domain.features.exporter.tournament.model.PlayerInfo
import io.reactivex.Observable
import java.io.File

interface TournamentExporter {

    fun export(activityContext: Context, deckId: String, playerInfo: PlayerInfo): Observable<File>
}
