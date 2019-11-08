package com.r0adkll.deckbuilder

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.executor.GlideExecutor
import com.bumptech.glide.module.AppGlideModule
import com.caverock.androidsvg.SVG
import com.r0adkll.deckbuilder.cache.PermanentDiskCacheFactory
import com.r0adkll.deckbuilder.util.glide.palette.PaletteBitmap
import com.r0adkll.deckbuilder.util.glide.palette.PaletteBitmapTranscoder
import com.r0adkll.deckbuilder.util.glide.svg.SvgDecoder
import java.io.File
import java.io.InputStream

@GlideModule
class DeckGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setSourceExecutor(GlideExecutor.newSourceExecutor(THREAD_COUNT, EXTRACTOR_NAME,
            GlideExecutor.UncaughtThrowableStrategy.IGNORE))

        val cacheDir = File(context.cacheDir, "images")
        builder.setDiskCache(PermanentDiskCacheFactory.create(cacheDir, true))
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.register(Bitmap::class.java, PaletteBitmap::class.java, PaletteBitmapTranscoder(glide))
        registry.append(InputStream::class.java, SVG::class.java, SvgDecoder())
    }

    companion object {

        private const val THREAD_COUNT = 4
        private const val EXTRACTOR_NAME = "glide-multi-executor"
    }
}
