package com.r0adkll.deckbuilder


import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.executor.GlideExecutor
import com.bumptech.glide.module.AppGlideModule
import com.caverock.androidsvg.SVG
import com.r0adkll.deckbuilder.util.glide.palette.PaletteBitmap
import com.r0adkll.deckbuilder.util.glide.palette.PaletteBitmapTranscoder
import com.r0adkll.deckbuilder.util.glide.svg.SvgDecoder
import java.io.InputStream


@GlideModule
class DeckGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setSourceExecutor(GlideExecutor.newSourceExecutor(4, "glide-multi-executor", GlideExecutor.UncaughtThrowableStrategy.IGNORE))

        val diskCacheSize = 1024L * 1024L * 500L
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, diskCacheSize))
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.register(Bitmap::class.java, PaletteBitmap::class.java, PaletteBitmapTranscoder(glide))
        registry.append(InputStream::class.java, SVG::class.java, SvgDecoder())
    }
}
