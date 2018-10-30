package com.r0adkll.deckbuilder.arch.data.features.offline.repository

import android.content.Context
import com.jakewharton.rxrelay2.BehaviorRelay
import com.r0adkll.deckbuilder.arch.data.features.offline.service.CacheService
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.DownloadRequest
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.OfflineStatus
import com.r0adkll.deckbuilder.arch.domain.features.offline.repository.OfflineRepository
import io.reactivex.Observable
import javax.inject.Inject


class DefaultOfflineRepository @Inject constructor(
        val context: Context
) : OfflineRepository, OfflineStatusConsumer {

    override var status = OfflineStatus()
        set(value) {
            field = value
            statusRelay.accept(value)
        }

    private val statusRelay = BehaviorRelay.createDefault(status).toSerialized()


    override fun download(request: DownloadRequest) {
        CacheService.start(context, request)
    }

    override fun observeStatus(): Observable<OfflineStatus> {
        return statusRelay
    }
}