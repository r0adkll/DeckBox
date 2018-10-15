package com.r0adkll.deckbuilder.arch.data.features.community.cache

import com.r0adkll.deckbuilder.arch.domain.features.community.model.DeckTemplate
import com.r0adkll.deckbuilder.util.Schedulers
import io.reactivex.Observable
import javax.inject.Inject


class FirestoreCommunityCache @Inject constructor(
        val schedulers: Schedulers
) : CommunityCache {

    override fun getDeckTemplates(): Observable<List<DeckTemplate>> {
        return Observable.just(emptyList())
    }
}