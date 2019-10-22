package com.r0adkll.deckbuilder.util.extensions

import java.text.NumberFormat

fun Double.formatPrice(): String = NumberFormat.getCurrencyInstance().format(this)
