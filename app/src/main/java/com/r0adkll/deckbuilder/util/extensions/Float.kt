@file:Suppress("MagicNumber")

package com.r0adkll.deckbuilder.util.extensions

import kotlin.math.roundToInt

val Float.readablePercentage: Int
    get() = times(100f).roundToInt().coerceIn(0..100)

fun Float.partialPercentage(part: Float): Float =
    (this - (1f - part)).coerceAtLeast(0f) / part

val Float.safePercentage: Float
    get() = coerceIn(0f..1f)
