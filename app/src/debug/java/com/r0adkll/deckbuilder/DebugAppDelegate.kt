package com.r0adkll.deckbuilder


import android.app.Application
import com.facebook.stetho.Stetho
import com.r0adkll.deckbuilder.internal.AppDelegate
import timber.log.Timber


class DebugAppDelegate : AppDelegate {

    override fun onCreate(app: Application) {
        Timber.plant(Timber.DebugTree())
        Stetho.initializeWithDefaults(app)
    }
}