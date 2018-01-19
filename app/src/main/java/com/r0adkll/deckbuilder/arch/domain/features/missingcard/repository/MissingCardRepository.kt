package com.r0adkll.deckbuilder.arch.domain.features.missingcard.repository


import com.r0adkll.deckbuilder.arch.domain.features.missingcard.model.MissingCard
import io.reactivex.Observable


interface MissingCardRepository {

    fun reportMissingCard(missingCard: MissingCard): Observable<Unit>
}