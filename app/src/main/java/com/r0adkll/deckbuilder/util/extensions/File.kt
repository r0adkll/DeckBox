package com.r0adkll.deckbuilder.util.extensions

import java.io.File
import java.io.IOException

fun File.deleteContents() {
    this.listFiles()?.forEach { file ->
        if (file.isDirectory) {
            file.deleteRecursively()
        }
        if (!file.delete()) {
            throw IOException("failed to delete file: $file")
        }
    }
}
