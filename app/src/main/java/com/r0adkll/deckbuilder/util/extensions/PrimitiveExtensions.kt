package com.r0adkll.deckbuilder.util.extensions


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.util.PatternsCompat
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.util.Base64
import com.ftinc.kit.kotlin.extensions.dipToPx
import com.ftinc.kit.kotlin.extensions.dpToPx
import timber.log.Timber


fun String.fromHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this)
    }
}


fun CharSequence.isEmail(): Boolean {
    return PatternsCompat.EMAIL_ADDRESS.matcher(this).find()
}


fun String?.formattedPartName(): String {
    return this?.let { "$it " } ?: ""
}


fun String.rawFromHtml(): String {
    val documentSpan = this.fromHtml()
    val chars = CharArray(documentSpan.length)
    TextUtils.getChars(documentSpan, 0, documentSpan.length, chars, 0)
    return String(chars)
}


fun Long.avatarPad(): String {
    return String.format("%010d", this)
}


fun Int.max(other: Int): Int = Math.max(this, other)


fun Float.dip(context: Context): Int = context.dipToPx(this)
fun Float.dp(context: Context): Float = context.dpToPx(this)