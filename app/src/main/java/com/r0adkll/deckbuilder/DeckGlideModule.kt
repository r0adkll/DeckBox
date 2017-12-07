package com.r0adkll.deckbuilder


import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.r0adkll.deckbuilder.util.palette.PaletteBitmap
import com.r0adkll.deckbuilder.util.palette.PaletteBitmapTranscoder


@GlideModule
class DeckGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.register(Bitmap::class.java, PaletteBitmap::class.java, PaletteBitmapTranscoder(glide))
    }
}