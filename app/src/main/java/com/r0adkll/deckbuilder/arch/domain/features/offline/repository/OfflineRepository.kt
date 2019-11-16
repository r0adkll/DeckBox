package com.r0adkll.deckbuilder.arch.domain.features.offline.repository

import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.DownloadRequest
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.OfflineStatus
import io.reactivex.Observable

interface OfflineRepository {

    /**
     * Start a request to download card or image data for offline use
     * @param request the download request specifying what you want to download
     */
    fun download(request: DownloadRequest)

    /**
     * Delete an individual expansion's cache, or the entire cache if [expansion] is null
     */
    fun delete(expansion: Expansion?): Observable<Unit>

    /**
     * Observe the current download/caching status of any current downloads
     */
    fun observeStatus(): Observable<OfflineStatus>
}
