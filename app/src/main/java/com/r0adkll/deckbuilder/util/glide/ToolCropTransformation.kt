package com.r0adkll.deckbuilder.util.glide

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.nio.charset.Charset
import java.security.MessageDigest

/**
 * Glide transformation for cropping the visual information out of Pokemon Tool item cards for displaying them
 * in the playtest card renders
 */
class ToolCropTransformation : BitmapTransformation() {

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val x = toTransform.width * X_SCALE
        val y = toTransform.height * Y_SCALE
        val width = toTransform.width * WIDTH_SCALE
        val height = toTransform.height * HEIGHT_SCALE
        return Bitmap.createBitmap(toTransform, x.toInt(), y.toInt(), width.toInt(), height.toInt())
    }

    override fun equals(o: Any?): Boolean {
        return o is ToolCropTransformation
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    companion object {
        private const val ID = "com.r0adkll.deckbuilder.util.glide.ToolCropTransformation"
        private val ID_BYTES = ID.toByteArray(Charset.forName("UTF-8"))

        private const val X_SCALE = 0.07496463932f
        private const val Y_SCALE = 0.147f
        private const val WIDTH_SCALE = 0.8429985856f
        private const val HEIGHT_SCALE = 0.353
    }
}
