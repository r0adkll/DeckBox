package com.r0adkll.deckbuilder.util.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.net.toUri
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.r0adkll.deckbuilder.GlideRequest
import com.r0adkll.deckbuilder.GlideRequests
import com.r0adkll.deckbuilder.arch.data.features.offline.service.ImageCacheLoader
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.PokemonCard
import com.r0adkll.deckbuilder.util.extensions.ImageType.BOTH
import com.r0adkll.deckbuilder.util.extensions.ImageType.HI_RES
import com.r0adkll.deckbuilder.util.extensions.ImageType.NORMAL

enum class ImageType {
    NORMAL,
    HI_RES,
    BOTH
}

fun GlideRequests.loadPokemonCard(context: Context, card: PokemonCard, type: ImageType): GlideRequest<Drawable> {
    val imageUri = when (type) {
        NORMAL -> card.imageUrl
        HI_RES -> card.imageUrlHiRes
        BOTH -> card.imageUrlHiRes
    }.toUri()

    var cacheFile = ImageCacheLoader.getCacheFile(context, imageUri)
    if (type == BOTH && cacheFile?.exists() != true) {
        cacheFile = ImageCacheLoader.getCacheFile(context, card.imageUrl.toUri())
    }

    return if (cacheFile?.exists() == true) {
        this.load(cacheFile)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
    } else {
        this.load(imageUri)
    }
}

fun GlideRequests.loadOfflineUri(context: Context, url: String?): GlideRequest<Drawable> {
    return if (url != null) {
        val cacheFile = ImageCacheLoader.getCacheFile(context, url?.toUri()!!)
        if (cacheFile?.exists() == true) {
            this.load(cacheFile)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        } else {
            this.load(url)
        }
    } else {
        this.load(url as String?)
    }
}

fun <T> GlideRequest<T>.loadOfflineUri(context: Context, url: String?): GlideRequest<T> {
    return if (url != null) {
        val cacheFile = ImageCacheLoader.getCacheFile(context, url.toUri())
        if (cacheFile?.exists() == true) {
            this.load(cacheFile)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        } else {
            this.load(url)
        }
    } else {
        this.load(url as String?)
    }
}
