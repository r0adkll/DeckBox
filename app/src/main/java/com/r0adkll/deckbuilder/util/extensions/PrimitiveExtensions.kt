package com.r0adkll.deckbuilder.util.extensions

import android.os.Build
import android.text.Html
import android.text.Spanned
import kotlin.math.max

fun String.fromHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this)
    }
}

fun Int.max(other: Int): Int = max(this, other)
