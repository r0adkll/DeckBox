package com.r0adkll.deckbuilder.arch.ui.features.browser


import com.r0adkll.deckbuilder.arch.domain.features.cards.repository.CardRepository
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.DownloadRequest
import com.r0adkll.deckbuilder.arch.domain.features.offline.repository.OfflineRepository
import com.r0adkll.deckbuilder.arch.ui.components.presenter.Presenter
import com.r0adkll.deckbuilder.arch.ui.features.browser.BrowseUi.State.*
import com.r0adkll.deckbuilder.arch.ui.features.browser.BrowseUi.State
import com.r0adkll.deckbuilder.util.extensions.logState
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import timber.log.Timber
import javax.inject.Inject


class BrowsePresenter @Inject constructor(
        val ui: BrowseUi,
        val intentions: BrowseUi.Intentions,
        val repository: CardRepository,
        val offlineRepository: OfflineRepository
) : Presenter() {

    override fun start() {

        val loadExpansions = repository.getExpansions()
                .map { it.reversed() }
                .map { Change.ExpansionsLoaded(it) as Change }
                .startWith(Change.IsLoading as Change)
                .onErrorReturn(handleUnknownError)

        val refreshExpansions = intentions.refreshExpansions()
                .flatMap { _ ->
                    repository.refreshExpansions()
                            .map { it.reversed() }
                            .map { Change.ExpansionsLoaded(it) as Change }
                            .startWith(Change.IsLoading as Change)
                            .onErrorReturn(handleUnknownError)
                }

        val offlineStatus = offlineRepository.observeStatus()
                .map { Change.OfflineStatusUpdated(it) as Change }

        val merged = loadExpansions
                .mergeWith(offlineStatus)
                .mergeWith(refreshExpansions)
                .doOnNext { Timber.d(it.logText) }

        disposables += merged.scan(ui.state, State::reduce)
                .logState()
                .subscribe { ui.render(it) }

        disposables += intentions.downloadExpansion()
                .subscribe {
                    offlineRepository.download(DownloadRequest(listOf(it), true))
                }

        disposables += intentions.downloadFormatExpansions()
                .subscribe {
                    offlineRepository.download(DownloadRequest(it, true))
                }
    }


    companion object {
        val handleUnknownError: (Throwable) -> Change = {
            Timber.e(it, "Error loading expansions")
            Change.Error("Error loading expansions")
        }
    }
}