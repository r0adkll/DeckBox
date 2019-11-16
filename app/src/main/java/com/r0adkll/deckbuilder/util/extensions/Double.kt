package com.r0adkll.deckbuilder.util.extensions

import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.round
import kotlin.math.roundToInt

fun Double.formatPrice(): String = NumberFormat.getCurrencyInstance().format(this)

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

fun Double.formatToMinDecimals(): String {
    val format = DecimalFormat("#.##")
    return format.format(this)
}
