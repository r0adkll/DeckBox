package com.r0adkll.deckbuilder.util.glide

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.nio.charset.Charset
import java.security.MessageDigest


class EnergyCropTransformation : BitmapTransformation() {


    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val radius = toTransform.width * RADIUS_SCALE
        val yOffset = toTransform.height * Y_SCALE

        val x = toTransform.width - radius
        val y = yOffset
        val size = (radius * 2f).toInt()

        val result = Bitmap.createBitmap(toTransform, x.toInt(), y.toInt(), size, size)
        return TransformationUtils.circleCrop(pool, result, outWidth, outHeight)
    }

    override fun equals(o: Any?): Boolean {
        return o is EnergyCropTransformation
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    companion object {
        private const val ID = "com.r0adkll.deckbuilder.util.glide.EnergyCropTransformation"
        private val ID_BYTES = ID.toByteArray(Charset.forName("UTF-8"))

        private const val RADIUS_SCALE = 0.7795918367f / 2f
        private const val Y_SCALE = 0.1140350877f
    }
}