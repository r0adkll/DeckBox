package com.r0adkll.deckbuilder.util

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashlyticsTree : Timber.DebugTree() {

    override fun e(t: Throwable) {
        FirebaseCrashlytics.getInstance()
            .recordException(t)
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val readablePriority = when (priority) {
            Log.VERBOSE -> "VERBOSE"
            Log.DEBUG -> "DEBUG"
            Log.INFO -> "INFO"
            Log.ASSERT -> "ASSERT"
            Log.WARN -> "WARN"
            Log.ERROR -> "ERROR"
            else -> "$priority"
        }

        FirebaseCrashlytics.getInstance()
            .log("$readablePriority:($tag) $message")

        if (t != null) {
            FirebaseCrashlytics.getInstance()
                .log(Log.getStackTraceString(t))
        }
    }
}
