package com.r0adkll.deckbuilder.cache

import com.bumptech.glide.load.engine.cache.DiskCache
import java.io.File

class PermanentDiskCacheFactory private constructor(
    private val directory: File,
    private val debug: Boolean = false
) : DiskCache.Factory {

    override fun build(): DiskCache? {
        return PermanentDiskCache(directory, debug)
    }

    companion object {

        fun create(directory: File, debug: Boolean = false): DiskCache.Factory {
            return PermanentDiskCacheFactory(directory, debug)
        }
    }
}
