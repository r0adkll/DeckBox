package com.r0adkll.deckbuilder.util

import com.crashlytics.android.Crashlytics
import timber.log.Timber

class CrashlyticsTree : Timber.DebugTree() {

    override fun e(t: Throwable) {
        Crashlytics.logException(t)
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (message.length < MAX_LOG_LENGTH) {
            Crashlytics.log(priority, tag, message)
            return
        }

        // Split by line, then ensure each line can fit into Log's maximum length.
        var i = 0
        val length = message.length
        while (i < length) {
            var newline = message.indexOf('\n', i)
            newline = if (newline != -1) newline else length
            do {
                val end = Math.min(newline, i + MAX_LOG_LENGTH)
                val part = message.substring(i, end)
                Crashlytics.log(priority, tag, part)
                i = end
            } while (i < newline)
            i++
        }
    }

    companion object {
        private val MAX_LOG_LENGTH = 4000
    }
}
