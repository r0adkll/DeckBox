package com.r0adkll.deckbuilder.arch.data.features.offline.service

import android.content.Context
import com.r0adkll.deckbuilder.arch.data.features.cards.cache.CardCache
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.DownloadRequest
import io.pokemontcg.Pokemon
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class ExpansionCacheLoader @Inject constructor(
    val context: Context,
    val api: Pokemon,
    val cardCache: CardCache
) {

    private val imageCacheLoader = ImageCacheLoader(context)

    suspend fun load(expansion: Expansion, request: DownloadRequest, progressListener: (Float) -> Unit): Result<Unit> {
        try {
            val expansionCards = api.card().where {
                setCode = expansion.code
                pageSize = PAGE_SIZE
            }.all()

            cardCache.putCards(expansionCards)

            // Download all images
            imageCacheLoader.download(expansionCards, request, progressListener)

            return Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Something went wrong when downloading ${expansion.code}")
            return Result.failure(e)
        }
    }

    companion object {
        private const val PAGE_SIZE = 1000
    }
}
