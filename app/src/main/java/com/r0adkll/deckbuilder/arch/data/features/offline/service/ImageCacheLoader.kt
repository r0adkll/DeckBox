package com.r0adkll.deckbuilder.arch.data.features.offline.service

import android.content.Context
import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.core.net.toUri
import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.arch.domain.features.offline.model.DownloadRequest
import io.pokemontcg.model.Card
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import okio.buffer
import okio.sink
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.concurrent.Executors

class ImageCacheLoader(
    val context: Context,
    val downloadDispatcher: CoroutineDispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
) {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        })
        .build()

    suspend fun download(
        cards: List<Card>,
        request: DownloadRequest,
        progressListener: (Float) -> Unit
    ) = withContext(Dispatchers.IO) {
        val imageUris = getImageUrls(cards, request)
        val count = imageUris.size.toFloat()
        val deferredDownloads = imageUris.map { uri ->
            async(downloadDispatcher) {
                val outputFile = getCacheFile(context, uri)
                if (outputFile != null) {
                    downloadImage(uri, outputFile)
                    outputFile
                } else {
                    Timber.w("Unable to build output file for $uri")
                    null
                }
            }
        }

        deferredDownloads.forEachIndexed { index, deferred ->
            deferred.await()

            val progress = (index + 1).toFloat() / count
            progressListener(progress)
        }
    }

    @WorkerThread
    private fun downloadImage(uri: Uri, output: File) {
        try {
            // Delete old file if exists then create new file to download to
            if (output.parentFile?.exists() == false) {
                output.parentFile?.mkdirs()
            }
            if (output.exists()) {
                output.delete()
            }
            output.createNewFile()

            val request = Request.Builder()
                .url(uri.toString())
                .get()
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                response.body?.let { responseBody ->
                    responseBody.source().use { source ->
                        output.sink().buffer().use { sink ->
                            source.readAll(sink)
                        }
                    }
                }
            } else {
                Timber.e("Error downloading image")
            }

        } catch (e: IOException) {
            Timber.e(e, "Error downloading ($uri) ==> $output")
        }

    }

    private fun getImageUrls(cards: List<Card>, request: DownloadRequest): List<Uri> {
        return cards.flatMap {
            val imageUrls = mutableListOf(it.imageUrl.toUri())
            if (request.includeHiRes) {
                imageUrls += it.imageUrlHiRes.toUri()
            }
            imageUrls
        }
    }

    companion object {
        fun getCacheFile(context: Context, uri: Uri): File? {
            val dir = File(context.cacheDir, "offline")
            dir.mkdir()
            return if (uri.path != null) {
                File(dir, uri.path!!)
            } else {
                null
            }
        }
    }
}
