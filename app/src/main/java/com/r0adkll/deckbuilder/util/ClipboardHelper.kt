package com.r0adkll.deckbuilder.util

import android.content.ClipboardManager
import android.content.Context
import com.r0adkll.deckbuilder.arch.data.features.importer.parser.LineValidator

object ClipboardHelper  {

    @Suppress("NestedBlockDepth")
    fun getDeckInClipboard(context: Context): String? {
        val lineValidator = LineValidator()
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val primaryClip = clipboardManager.primaryClip
        if (primaryClip != null) {
            for (index in 0 until primaryClip.itemCount) {
                val clipText = primaryClip.getItemAt(index)?.coerceToText(context)?.toString()
                if (clipText != null) {
                    val clipLines = clipText.split("\n")
                    if (clipLines.any { lineValidator.validate(it) != null}){
                        return clipText
                    }
                }
            }
        }

        return null
    }
}
