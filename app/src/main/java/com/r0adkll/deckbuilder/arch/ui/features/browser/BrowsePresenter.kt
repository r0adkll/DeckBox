package com.r0adkll.deckbuilder.arch.ui.features.browser

import android.annotation.SuppressLint
import com.ftinc.kit.arch.presentation.presenter.UiPresenter
import com.ftinc.kit.arch.util.plusAssign
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.expansions.repository.ExpansionRepository
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.DownloadRequest
import com.r0adkll.deckbuilder.arch.domain.features.offline.repository.OfflineRepository
import com.r0adkll.deckbuilder.arch.ui.components.ExpansionComparator
import com.r0adkll.deckbuilder.arch.ui.features.browser.BrowseUi.State
import com.r0adkll.deckbuilder.arch.ui.features.browser.BrowseUi.State.Change
import com.r0adkll.deckbuilder.util.extensions.fromReleaseDate
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class BrowsePresenter @Inject constructor(
  ui: BrowseUi,
  val intentions: BrowseUi.Intentions,
  val expansionRepository: ExpansionRepository,
  val offlineRepository: OfflineRepository,
  val preferences: AppPreferences
) : UiPresenter<State, Change>(ui) {

  @SuppressLint("RxSubscribeOnError")
  override fun smashObservables(): Observable<Change> {

    val loadExpansions = expansionRepository.getExpansions()
      .map(::sortExpansions)
      .map { Change.ExpansionsLoaded(it) as Change }
      .startWith(Change.IsLoading as Change)
      .onErrorReturn(handleUnknownError)

    val refreshExpansions = intentions.refreshExpansions()
      .flatMap { _ ->
        expansionRepository.refreshExpansions()
          .map(::sortExpansions)
          .map { Change.ExpansionsLoaded(it) as Change }
          .startWith(Change.IsLoading as Change)
          .onErrorReturn(handleUnknownError)
      }

    val offlineStatus = offlineRepository.observeStatus()
      .map { Change.OfflineStatusUpdated(it) as Change }

    val offlineOutline = preferences.offlineOutline
      .asObservable()
      .map { Change.OfflineOutline(it) as Change }

    disposables += intentions.downloadExpansion()
      .subscribe {
        offlineRepository.download(DownloadRequest(listOf(it), true))
      }

    disposables += intentions.downloadFormatExpansions()
      .subscribe {
        offlineRepository.download(DownloadRequest(it, true))
      }

    disposables += intentions.hideOfflineOutline()
      .subscribe {
        preferences.offlineOutline.set(false)
      }

    return loadExpansions
      .mergeWith(offlineStatus)
      .mergeWith(offlineOutline)
      .mergeWith(refreshExpansions)
  }

  private fun sortExpansions(expansions: List<Expansion>): List<Expansion> {
    return expansions.sortedByDescending {
      it.releaseDate.fromReleaseDate()
    }
  }

  companion object {
    val handleUnknownError: (Throwable) -> Change = {
      Timber.e(it, "Error loading expansions")
      Change.Error("Error loading expansions")
    }
  }
}
