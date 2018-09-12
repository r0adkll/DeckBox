package com.r0adkll.deckbuilder


import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.caverock.androidsvg.SVG
import com.r0adkll.deckbuilder.util.palette.PaletteBitmap
import com.r0adkll.deckbuilder.util.palette.PaletteBitmapTranscoder
import com.r0adkll.deckbuilder.util.svg.SvgDecoder
import com.r0adkll.deckbuilder.util.svg.SvgDrawableTranscoder
import java.io.InputStream


@GlideModule
class DeckGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.register(Bitmap::class.java, PaletteBitmap::class.java, PaletteBitmapTranscoder(glide))
        registry//.register(SVG::class.java, PictureDrawable::class.java, SvgDrawableTranscoder())
                .append(InputStream::class.java, SVG::class.java, SvgDecoder())
    }
}