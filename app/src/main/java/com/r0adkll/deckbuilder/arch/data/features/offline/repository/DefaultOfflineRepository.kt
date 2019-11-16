package com.r0adkll.deckbuilder.arch.data.features.offline.repository

import android.content.Context
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.offline.cache.OfflineCache
import com.r0adkll.deckbuilder.arch.data.features.offline.service.CacheService
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.CacheStatus
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.DownloadRequest
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.OfflineStatus
import com.r0adkll.deckbuilder.arch.domain.features.offline.repository.OfflineRepository
import com.r0adkll.deckbuilder.internal.di.scopes.AppScope
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

@AppScope
class DefaultOfflineRepository @Inject constructor(
    val context: Context,
    val preferences: AppPreferences,
    val cache: OfflineCache
) : OfflineRepository, OfflineStatusConsumer {

    override var status = OfflineStatus()
        set(value) {
            field = value
            statusRelay.accept(value)
        }

    private var offlineStatusDisposable: Disposable? = null
    private val statusRelay: Relay<OfflineStatus> = BehaviorRelay.create<OfflineStatus>().toSerialized()

    init {
        offlineStatusDisposable = cache.getOfflineStatus()
            .subscribe({
                status = it
            }, {
                Timber.w(it, "Unable to get the current cache status")
            })
    }

    override fun download(request: DownloadRequest) {
        // Filter any expansions that we have marked as cached
        val filteredExpansions = request.expansion.filter {
            val status = status.expansions[it]
            status == null || status is CacheStatus.Empty
        }

        // Flag all expansions in request
        var statusChanges = status
        filteredExpansions.forEach {
            statusChanges = statusChanges.set(it to CacheStatus.Queued)
        }
        status = statusChanges

        CacheService.start(context, request.copy(expansion = filteredExpansions))
    }

    override fun delete(expansion: Expansion?): Observable<Unit> {
        return cache.delete(expansion)
            .flatMap {
                cache.getOfflineStatus()
                    .doOnNext {
                        status = it
                    }
                    .map { Unit }
            }
    }

    override fun observeStatus(): Observable<OfflineStatus> {
        return statusRelay
            .observeOn(AndroidSchedulers.mainThread())
    }
}
