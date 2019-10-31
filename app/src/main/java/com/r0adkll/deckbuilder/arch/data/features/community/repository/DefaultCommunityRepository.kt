package com.r0adkll.deckbuilder.arch.data.features.community.repository

import com.r0adkll.deckbuilder.arch.data.features.community.cache.CommunityCache
import com.r0adkll.deckbuilder.arch.domain.features.community.model.DeckTemplate
import com.r0adkll.deckbuilder.arch.domain.features.community.repository.CommunityRepository
import com.r0adkll.deckbuilder.util.AppSchedulers
import io.reactivex.Observable
import javax.inject.Inject

class DefaultCommunityRepository @Inject constructor(
        val cache: CommunityCache,
        val schedulers: AppSchedulers
) : CommunityRepository {

    override fun getDeckTemplates(): Observable<List<DeckTemplate>> {
        return cache.getDeckTemplates()
    }
}
