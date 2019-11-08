package com.r0adkll.deckbuilder.cache

import com.bumptech.glide.load.Key
import java.nio.ByteBuffer
import java.security.MessageDigest

data class CardImageKey(
    val setCode: String,
    val cardId: String,
    val imageType: Type
) : Key {

    enum class Type {
        NORMAL,
        HI_RES
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        val size = setCode.toByteArray().size +
            cardId.toByteArray().size +
            Int.SIZE_BYTES
        messageDigest.update(
            ByteBuffer.allocate(size)
                .put(setCode.toByteArray())
                .put(cardId.toByteArray())
                .putInt(imageType.ordinal)
        )
    }
}
