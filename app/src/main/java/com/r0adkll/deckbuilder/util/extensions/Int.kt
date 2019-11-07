package com.r0adkll.deckbuilder.util.extensions

import android.graphics.Color
import androidx.core.graphics.ColorUtils

/**
 * Determine whether or not `this` color is light or dark
 */
@Suppress("MagicNumber")
val Int.isLight: Boolean
    get() = ColorUtils.calculateContrast(Color.WHITE, this) < 3.0
