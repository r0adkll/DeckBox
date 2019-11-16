package com.r0adkll.deckbuilder.util.extensions

import java.text.DecimalFormat
import java.text.NumberFormat

fun Double.formatPrice(): String = NumberFormat.getCurrencyInstance().format(this)

fun Double.formatToMinDecimals(): String {
    val format = DecimalFormat("#.##")
    return format.format(this)
}
