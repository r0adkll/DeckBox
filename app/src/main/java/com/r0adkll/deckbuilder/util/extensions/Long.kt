package com.r0adkll.deckbuilder.util.extensions

fun Long.readableFileSize(): String {
    val (size, label) = this.displayableFileSize()
    return "$size $label"
}

fun Long.displayableFileSize(): Pair<String, String> {
    return when {
        this < KILOBYTE -> "$this" to "b"
        this < MEGABYTE -> {
            val kilobytes = this.toDouble() / KILOBYTE.toDouble()
            kilobytes.formatToMinDecimals() to "Kb"
        }
        this < GIGABYTE -> {
            val megabytes = this.toDouble() / MEGABYTE.toDouble()
            megabytes.formatToMinDecimals() to "Mb"
        }
        else -> {
            val gigabytes = this.toDouble() / GIGABYTE.toDouble()
            gigabytes.formatToMinDecimals() to "Gb"
        }
    }
}

const val KILOBYTE = 1024L
const val MEGABYTE = KILOBYTE * 1024L
const val GIGABYTE = MEGABYTE * 1024L
