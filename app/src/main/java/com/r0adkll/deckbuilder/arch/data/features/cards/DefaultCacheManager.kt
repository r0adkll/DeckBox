package com.r0adkll.deckbuilder.arch.data.features.cards


import android.content.Context
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.cards.service.CacheService
import com.r0adkll.deckbuilder.arch.domain.features.cards.CacheManager
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.CacheStatus
import com.r0adkll.deckbuilder.internal.di.scopes.AppScope
import io.reactivex.Observable
import javax.inject.Inject


@AppScope
class DefaultCacheManager @Inject constructor(
        val context: Context,
        val preferences: AppPreferences
) : CacheManager {

    private val cacheStatus: Relay<CacheStatus> =
            BehaviorRelay.createDefault(if (preferences.offlineEnabled) CacheStatus.Cached else CacheStatus.Empty)


    override fun startCaching() {
        CacheService.start(context)
    }


    override fun updateCacheStatus(status: CacheStatus) {
        cacheStatus.accept(status)
    }


    override fun observeCacheStatus(): Observable<CacheStatus> {
        return cacheStatus
    }
}