package com.r0adkll.deckbuilder.util.extensions

import android.content.Intent
import android.os.Parcel

/**
 * Get the data size in Bytes for this intent when it is parceled
 */
fun Intent.bytes(): Int {
    val p = Parcel.obtain()
    this.writeToParcel(p, 0)
    val bytes = p.dataSize()
    p.recycle()
    return bytes
}
