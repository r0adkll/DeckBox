package com.r0adkll.deckbuilder.arch.data.features.offline.cache

import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.OfflineStatus
import io.reactivex.Observable

interface OfflineCache {

    fun getOfflineStatus(): Observable<OfflineStatus>
    fun delete(expansion: Expansion?): Observable<Unit>
}
