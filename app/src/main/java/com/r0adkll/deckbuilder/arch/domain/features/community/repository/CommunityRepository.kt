package com.r0adkll.deckbuilder.arch.domain.features.community.repository


import com.r0adkll.deckbuilder.arch.domain.features.community.model.DeckTemplate
import io.reactivex.Observable


interface CommunityRepository {

    fun getDeckTemplates(): Observable<List<DeckTemplate>>
}