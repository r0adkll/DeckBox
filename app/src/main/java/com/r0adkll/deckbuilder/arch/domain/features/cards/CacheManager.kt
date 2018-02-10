package com.r0adkll.deckbuilder.arch.domain.features.cards


import com.r0adkll.deckbuilder.arch.domain.features.cards.model.CacheStatus
import io.reactivex.Observable


interface CacheManager {

    fun startCaching()
    fun updateCacheStatus(status: CacheStatus)
    fun observeCacheStatus(): Observable<CacheStatus>
}