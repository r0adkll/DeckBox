package com.r0adkll.deckbuilder

import android.app.Application
import com.ftinc.kit.app.AppDelegate
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.firebase.FirebaseAnalyticInterface
import com.r0adkll.deckbuilder.util.CrashlyticsAnalyticInterface
import com.r0adkll.deckbuilder.util.CrashlyticsTree
import timber.log.Timber

class ReleaseAppDelegate : AppDelegate {

    override fun onCreate(app: Application) {
        installAnalytics(app)
        installCrashlytics()
    }

    private fun installAnalytics(app: Application) {
        Analytics.add(FirebaseAnalyticInterface(app))
        Analytics.add(CrashlyticsAnalyticInterface())
    }

    private fun installCrashlytics() {
        FirebaseCrashlytics.getInstance().apply {
            setCustomKey("GIT_HASH", BuildConfig.GIT_SHA)
        }
        Timber.plant(CrashlyticsTree())
    }
}
