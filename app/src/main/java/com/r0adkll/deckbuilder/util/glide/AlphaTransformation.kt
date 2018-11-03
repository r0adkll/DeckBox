package com.r0adkll.deckbuilder.util.glide

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation

import java.nio.charset.Charset
import java.security.MessageDigest


class AlphaTransformation(val alpha: Float) : BitmapTransformation() {

    private val paint = Paint()

    init {
        paint.alpha = (255f * alpha).toInt()
    }

    public override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val bitmap = pool.get(outWidth, outHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawBitmap(toTransform, 0f, 0f, paint)
        return bitmap
    }

    override fun equals(o: Any?): Boolean {
        return o is AlphaTransformation
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    companion object {
        private const val ID = "com.r0adkll.deckbuilder.util.glide.AlphaTransformation"
        private val ID_BYTES = ID.toByteArray(Charset.forName("UTF-8"))
    }
}