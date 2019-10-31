package com.r0adkll.deckbuilder.util.extensions

import android.content.Context

@Suppress("UNCHECKED_CAST")
fun <T> systemService(context: Context, name: String): Lazy<T> = lazy {
    context.getSystemService(name) as T
}
@Suppress("UNCHECKED_CAST")
fun <T> Context.service(name: String): Lazy<T> = lazy {
    this.getSystemService(name) as T
}
