package com.r0adkll.deckbuilder.arch.data.remote.plugin

import android.annotation.SuppressLint
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.database.DeckDatabase
import com.r0adkll.deckbuilder.arch.data.features.expansions.cache.ExpansionCache
import com.r0adkll.deckbuilder.arch.data.features.expansions.repository.source.PreviewExpansionDataSource
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import com.r0adkll.deckbuilder.util.AppSchedulers
import timber.log.Timber

class PreviewCacheInvalidatePlugin(
    val expansionDataSource: PreviewExpansionDataSource,
    val db: DeckDatabase,
    val preferences: AppPreferences,
    val schedulers: AppSchedulers
) : RemotePlugin {

    @SuppressLint("CheckResult", "RxLeakedSubscription")
    override fun onFetchActivated(remote: Remote) {
        val expansionVersion = remote.expansionVersion
        val previewExpansionVersion = remote.previewExpansionVersion

        if (expansionVersion != null && previewExpansionVersion != null) {
            if (previewExpansionVersion.expansionCode == expansionVersion.expansionCode) {
                /*
                 * Looks like the API is now supporting our preview expansion. We need to:
                 *
                 * 1. Remove preview expansions
                 * 2. Remove preview cards cache.
                 * 3. Clear preview card image cache
                 */
                expansionDataSource.clear()
                db.cards().clearPreviewCards()
                    .subscribeOn(schedulers.disk)
                    .subscribe({
                        Timber.i("Preview cards removed from cache")
                    }, {
                        Timber.e(it, "Error removing preview cards from cache")
                    })

                // We don't need to go any further
                return
            }
        }

        // Verify cache, and invalidate if needed
        remote.previewExpansionVersion?.let { (versionCode, expansionCode) ->
            Timber.d("""Checking Preview Expansion Cache (version: $versionCode, expansion: $expansionCode, 
                |prefVersion: ${preferences.previewExpansionsVersion})""".trimMargin())

            try {
                val invalidCache = expansionDataSource.getExpansions(ExpansionCache.Source.LOCAL)
                    .blockingFirst().none { it.code == expansionCode }

                if (versionCode > preferences.previewExpansionsVersion || invalidCache) {
                    Timber.i("""Preview Expansion Cache Invalidated, Refreshing from server 
                        |(version: $versionCode, expansion: $expansionCode)""".trimMargin())
                    expansionDataSource.refreshExpansions()
                        .subscribeOn(schedulers.network)
                        .subscribe({
                            Timber.d("""Expansions refreshed, 
                                |updating version(${preferences.expansionsVersion} > $versionCode)""".trimMargin())
                            preferences.previewExpansionsVersion = versionCode
                        }, {
                            Timber.e(it, "Unable to refresh preview expansions, keeping old cache")
                        })
                }
            } catch (e: NoSuchElementException) {
                Timber.e("Unable to verify local preview expansions")
            }
        }
    }
}
