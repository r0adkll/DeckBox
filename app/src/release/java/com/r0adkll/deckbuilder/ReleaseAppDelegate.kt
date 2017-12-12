package com.r0adkll.deckbuilder


import android.app.Application
import com.crashlytics.android.Crashlytics
import com.r0adkll.deckbuilder.internal.AppDelegate
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.firebase.FirebaseAnalyticInterface
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
    }


    private fun installFabric(app: Application) {
        val fabric = Fabric.Builder(app)
                .kits(Crashlytics())
                .build()
        Fabric.with(fabric)
    }


    private fun installCrashlytics() {
        Crashlytics.setString("GIT_HASH", BuildConfig.GIT_SHA)
        Crashlytics.setString("GIT_TAG", BuildConfig.GIT_TAG)
        Crashlytics.setString("FLAVOR", BuildConfig.FLAVOR)
        Timber.plant(CrashlyticsTree())
    }
}