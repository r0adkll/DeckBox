package com.r0adkll.deckbuilder

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.ftinc.kit.app.AppDelegate
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.firebase.FirebaseAnalyticInterface
import com.r0adkll.deckbuilder.util.CrashlyticsAnalyticInterface
import com.r0adkll.deckbuilder.util.CrashlyticsTree
import io.fabric.sdk.android.Fabric
import timber.log.Timber

class ReleaseAppDelegate : AppDelegate {

    override fun onCreate(app: Application) {
        installFabric(app)
        installAnalytics(app)
        installCrashlytics()
    }

    private fun installAnalytics(app: Application) {
        Analytics.add(FirebaseAnalyticInterface(app))
        Analytics.add(CrashlyticsAnalyticInterface())
    }

    private fun installFabric(app: Application) {
        val fabric = Fabric.Builder(app)
            .kits(Crashlytics())
            .build()
        Fabric.with(fabric)
    }

    private fun installCrashlytics() {
        Crashlytics.setString("GIT_HASH", BuildConfig.GIT_SHA)
        Crashlytics.setString("FLAVOR", BuildConfig.FLAVOR)
        Timber.plant(CrashlyticsTree())
    }
}
