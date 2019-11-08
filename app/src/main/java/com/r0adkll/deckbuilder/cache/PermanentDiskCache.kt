package com.r0adkll.deckbuilder.cache

import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.cache.DiskCache
import com.bumptech.glide.load.engine.cache.SafeKeyGenerator
import com.r0adkll.deckbuilder.util.extensions.deleteContents
import timber.log.Timber
import java.io.File
import java.io.IOException

class PermanentDiskCache(
    val directory: File,
    var debug: Boolean = true
) : DiskCache {

    private val safeKeyGenerator = SafeKeyGenerator()
    private val writeLocker = DiskCacheWriteLocker()

    init {
        if (!directory.exists()) {
            directory.mkdirs()
        }
    }

    override fun clear() {
        try {
            directory.deleteContents()
        } catch (e: IOException) {
            runIf(debug) {
                Timber.e(e, "Unable to clear disk cache or disk cache cleared externally")
            }
        }
    }

    override fun put(key: Key?, writer: DiskCache.Writer) {
        val safeKey = safeKeyGenerator.getSafeKey(key)
        writeLocker.acquire(safeKey)
        try {
            runIf(debug) {
                Timber.v("Put: Obtained: $safeKey for key: $key")
            }
            try {
                val cacheFile = File(directory, safeKey)
                if (cacheFile.exists()) {
                    return
                }

                if (!writer.write(cacheFile)) {
                    runIf(debug) {
                        Timber.e("Unable to put to disk cache")
                    }
                }
            } catch (e: IOException) {
                runIf(debug) {
                    Timber.e(e, "Unable to put to disk cache")
                }
            }
        } finally {
            writeLocker.release(safeKey)
        }
    }

    override fun get(key: Key?): File? {
        val safeKey: String = safeKeyGenerator.getSafeKey(key)
        runIf(debug) {
            Timber.v("Get: Obtained: $safeKey for for Key: $key")
        }

        var result: File? = null
        try {
            result = File(directory, safeKey)
            if (!result.exists()) {
                result = null
            }
        } catch (e: IOException) {
            runIf(debug) {
                Timber.w(e, "Unable to get from disk cache")
            }
        }
        return result
    }

    override fun delete(key: Key?) {
        val safeKey: String? = safeKeyGenerator.getSafeKey(key)
        try {
            val file = File(directory, safeKey)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: IOException) {
            runIf(debug) {
                Timber.w(e, "Unable to delete from disk cache")
            }
        }
    }

    private fun runIf(condition: Boolean, action: () -> Unit) {
        if (condition) {
            action()
        }
    }
}
