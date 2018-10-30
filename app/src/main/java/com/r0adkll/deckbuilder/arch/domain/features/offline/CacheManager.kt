package com.r0adkll.deckbuilder.arch.domain.features.offline


import com.r0adkll.deckbuilder.arch.domain.features.offline.model.CacheStatus
import io.reactivex.Observable


interface CacheManager {

    fun startCaching()
    fun updateCacheStatus(status: CacheStatus)
    fun observeCacheStatus(): Observable<CacheStatus>
}