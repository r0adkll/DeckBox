package com.r0adkll.deckbuilder.arch.ui.features.settings.cache

import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.ftinc.kit.arch.util.plusAssign
import com.r0adkll.deckbuilder.arch.domain.features.offline.repository.OfflineRepository
import com.r0adkll.deckbuilder.arch.ui.features.settings.cache.ManageCacheUi.State
import com.r0adkll.deckbuilder.arch.ui.features.settings.cache.ManageCacheUi.State.Change
import com.r0adkll.deckbuilder.internal.di.scopes.ActivityScope
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

@ActivityScope
class ManageCachePresenter @Inject constructor(
    ui: ManageCacheUi,
    val intentions: ManageCacheUi.Intentions,
    val repository: OfflineRepository
) : UiPresenter<State, Change>(ui) {

    override fun smashObservables(): Observable<Change> {

        val offlineStatus = repository.observeStatus()
            .map { Change.CacheStatus(it) as Change }

        disposables += intentions.deleteAllCache()
            .flatMap {
                repository.delete(null)
            }
            .subscribe({
                Timber.d("Deleted ALL cache")
            }, {
                Timber.e(it, "Failed to delete entire cache")
            })

        disposables += intentions.deleteCache()
            .flatMap {
                repository.delete(it)
            }
            .subscribe({
                Timber.d("Cache deleted!")
            }, {
                Timber.e(it, "Failed to delete cache")
            })

        return offlineStatus
    }
}
