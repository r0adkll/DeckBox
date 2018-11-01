package com.r0adkll.deckbuilder.arch.data.features.offline.repository

import android.content.Context
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.offline.service.CacheService
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.CacheStatus
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.DownloadRequest
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.OfflineStatus
import com.r0adkll.deckbuilder.arch.domain.features.offline.repository.OfflineRepository
import com.r0adkll.deckbuilder.internal.di.scopes.AppScope
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject


@AppScope
class DefaultOfflineRepository @Inject constructor(
        val context: Context,
        val preferences: AppPreferences
) : OfflineRepository, OfflineStatusConsumer {

    override var status = OfflineStatus()
        set(value) {
            field = value
            statusRelay.accept(value)
        }

    private val statusRelay: Relay<OfflineStatus> = BehaviorRelay.create<OfflineStatus>().toSerialized()

    init {
        // Load saved preferences
        val offlineStatus = HashMap<String, CacheStatus>()
        preferences.offlineExpansions.get().forEach {
            offlineStatus[it] = CacheStatus.Cached
        }
        status = OfflineStatus(offlineStatus)
    }

    override fun download(request: DownloadRequest) {
        // Flag all expansions in request
        var statusChanges = status
        request.expansion.forEach {
            statusChanges = statusChanges.set(it.code to CacheStatus.Queued)
        }
        status = statusChanges

        // Start service
        CacheService.start(context, request)
    }

    override fun observeStatus(): Observable<OfflineStatus> {
        return statusRelay
                .observeOn(AndroidSchedulers.mainThread())
    }
}