package com.r0adkll.deckbuilder.arch.data.features.offline.cache

import android.content.Context
import com.r0adkll.deckbuilder.arch.data.database.DeckDatabase
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.expansions.repository.ExpansionRepository
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.CacheStatus
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.OfflineStatus
import com.r0adkll.deckbuilder.internal.di.scopes.AppScope
import com.r0adkll.deckbuilder.util.AppSchedulers
import io.reactivex.Observable
import java.io.File
import java.io.IOException
import javax.inject.Inject

@AppScope
class DefaultOfflineCache @Inject constructor(
    val context: Context,
    val db: DeckDatabase,
    val expansionRepository: ExpansionRepository,
    val schedulers: AppSchedulers
) : OfflineCache {

    override fun getOfflineStatus(): Observable<OfflineStatus> {
        return expansionRepository.getExpansions()
            .map { expansions ->
                val cacheStatus = mutableMapOf<Expansion, CacheStatus>()
                expansions.forEach { expansion ->
                    val cardCount = db.cards().getCount(expansion.code)

                    val cacheDir = ImageCacheLoader.getCacheDir(context)
                    val expansionCacheDir = File(cacheDir, expansion.code)

                    var cacheSizeBytes = 0L
                    var normalImageCount = 0
                    var hiResImageCount = 0

                    if (expansionCacheDir.exists()) {
                        expansionCacheDir.listFiles()?.forEach { file ->
                            cacheSizeBytes += file.length()

                            val fileName = file.name
                            if (fileName != "logo.png" && fileName != "symbol.png") {
                                if (fileName.contains("hires", true)) {
                                    hiResImageCount++
                                } else {
                                    normalImageCount++
                                }
                            }
                        }
                    }

                    if (cacheSizeBytes > 0L && normalImageCount > 0 && hiResImageCount > 0) {
                        cacheStatus[expansion] = CacheStatus.Cached(
                            cacheSizeBytes,
                            cardCount,
                            normalImageCount,
                            hiResImageCount
                        )
                    } else {
                        cacheStatus[expansion] = CacheStatus.Empty
                    }
                }
                OfflineStatus(cacheStatus)
            }
            .subscribeOn(schedulers.disk)
    }

    override fun delete(expansion: Expansion?): Observable<Unit> {
        return Observable.fromCallable {
            val cacheDir = ImageCacheLoader.getCacheDir(context)
            if (expansion != null) {
                val expansionCacheDir = File(cacheDir, expansion.code)
                if (expansionCacheDir.exists()) {
                    if (!expansionCacheDir.deleteRecursively()) {
                        throw IOException("Failed to delete all cached files")
                    }
                }
            } else {
                if (!cacheDir.deleteRecursively()) {
                    throw IOException("Failed to delete all cached files")
                }
            }
        }.subscribeOn(schedulers.disk)
    }
}
