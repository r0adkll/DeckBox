package com.r0adkll.deckbuilder.arch.data.features.community.cache

import com.r0adkll.deckbuilder.arch.domain.features.community.model.DeckTemplate
import io.reactivex.Observable

interface CommunityCache {

    fun getDeckTemplates(): Observable<List<DeckTemplate>>
}
