package com.r0adkll.deckbuilder.arch.data.features.offline.cache

import android.content.Context
import android.net.Uri
import androidx.annotation.WorkerThread
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.cache.DiskCache
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper
import com.bumptech.glide.load.model.GlideUrl
import com.r0adkll.deckbuilder.DeckGlideModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import okio.buffer
import okio.sink
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.security.MessageDigest
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

class ImageCacheLoader(
    val context: Context,
    downloadDispatcher: CoroutineDispatcher = Executors.newFixedThreadPool(NUM_DOWNLOAD_THREADS).asCoroutineDispatcher()
) {
    /**
     * This class is used to generate the expected file output hash that Glide
     * uses to store images in cache. We use this so we can delete those images when the user offlines them
     * using this class.
     */
    data class DataCacheKey(
        private val sourceKey: Key
    ) : Key {

        override fun updateDiskCacheKey(messageDigest: MessageDigest) {
            sourceKey.updateDiskCacheKey(messageDigest)
            // Do nothing for the signature, since we never set one
        }

        companion object {

            fun create(uri: Uri): DataCacheKey {
                val glideUrl = GlideUrl(uri.toString())
                return DataCacheKey(glideUrl)
            }
        }
    }

    private val downloadScope = CoroutineScope(downloadDispatcher)

    private val diskCache = DiskLruCacheWrapper.create(
        File(context.cacheDir, DiskCache.Factory.DEFAULT_DISK_CACHE_DIR),
        DeckGlideModule.DISK_CACHE_SIZE_IN_BYTES
    )

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.NONE
        })
        .build()

    suspend fun download(
        imageUris: List<Uri>,
        progressListener: (Float) -> Unit = { }
    ) {
        if (imageUris.isNotEmpty()) {
            val count = imageUris.size.toFloat()
            val successCounter = AtomicInteger(0)
            val failedCounter = AtomicInteger(0)
            val deferredDownloads = imageUris.map { uri ->
                downloadScope.async {
                    try {
                        val outputFile = getCacheFile(context, uri)
                        if (outputFile != null) {
                            downloadImage(uri, outputFile)
                            deleteGlideImageCache(uri)

                            // Update Progress
                            Timber.v("Downloaded $uri to $outputFile")
                            val completed = successCounter.incrementAndGet() + failedCounter.get()
                            val progress = (completed + 1f) / count
                            progressListener(progress)

                            outputFile
                        } else {
                            Timber.w("Unable to build output file for $uri")
                            failedCounter.incrementAndGet()
                            null
                        }
                    } catch (e: Exception) {
                        Timber.e(e, "Unable to download image @ $uri")
                        failedCounter.incrementAndGet()
                    }
                }
            }

            awaitAll(*deferredDownloads.toTypedArray())
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

    private fun deleteGlideImageCache(uri: Uri) {
        val key = DataCacheKey.create(uri)
        diskCache.delete(key)
    }

    companion object {
        private val NUM_DOWNLOAD_THREADS
            get() = Runtime.getRuntime().availableProcessors().coerceAtLeast(2)

        fun getCacheDir(context: Context): File {
            return File(context.cacheDir, "offline")
        }

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
