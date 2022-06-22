package com.r0adkll.deckbuilder.arch.data.features.offline.cache

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.r0adkll.deckbuilder.arch.data.features.cards.cache.CardCache
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.CacheStatus
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.DownloadRequest
import io.pokemontcg.Pokemon
import io.pokemontcg.model.Card
import io.pokemontcg.requests.query.CardBuilder
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class ExpansionCacheLoader @Inject constructor(
    val context: Context,
    val api: Pokemon,
    val cardCache: CardCache
) {

    private val imageCacheLoader = ImageCacheLoader(context)

    @Suppress("TooGenericExceptionCaught")
    suspend fun load(
        expansion: Expansion,
        request: DownloadRequest,
        progressListener: (Float) -> Unit
    ): Result<CacheStatus.Cached> {
        val card = CardBuilder()
        card.set {
            id(expansion.code)
        }
        try {
            val expansionCards = api.card().where {
                query = card.build()
                pageSize = PAGE_SIZE
            }.all()

            cardCache.putCards(expansionCards)

            // Download logo & symbol urls
            val expansionImageUris = listOfNotNull(
                expansion.logoUrl?.toUri(),
                expansion.symbolUrl.toUri()
            )
            imageCacheLoader.download(expansionImageUris)

            // Download all images
            val imageUris = getImageUrls(expansionCards, request)
            imageCacheLoader.download(imageUris, progressListener)

            val imageCacheDir = File(ImageCacheLoader.getCacheDir(context), expansion.code)
            val status = calculateCachedStatus(expansionCards.size, imageCacheDir)
            return Result.success(status)
        } catch (e: Exception) {
            Timber.e(e, "Something went wrong when downloading ${expansion.code}")
            return Result.failure(e)
        }
    }

    private fun calculateCachedStatus(numCards: Int, directory: File): CacheStatus.Cached {
        var sizeInBytes = 0L
        var normalImageCount = 0
        var hiResImageCount = 0
        directory.listFiles()?.forEach { file ->
            val fileName = file.name

            sizeInBytes += file.length()
            if (fileName != "logo.png" && fileName != "symbol.png") {
                if (file.name.contains("hires")) {
                    hiResImageCount++
                } else {
                    normalImageCount++
                }
            }
        }

        return CacheStatus.Cached(
            sizeInBytes,
            numCards,
            normalImageCount,
            hiResImageCount
        )
    }

    private fun getImageUrls(cards: List<Card>, request: DownloadRequest): List<Uri> {
        return cards.flatMap {
            val imageUrls = mutableListOf(it.images.small.toUri())
            if (request.includeHiRes) {
                imageUrls += it.images.large.toUri()
            }
            imageUrls
        }
    }

    companion object {
        private const val PAGE_SIZE = 1000
    }
}
