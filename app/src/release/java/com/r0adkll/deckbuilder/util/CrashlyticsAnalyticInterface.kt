package com.r0adkll.deckbuilder.util

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.r0adkll.deckbuilder.internal.analytics.AnalyticInterface
import com.r0adkll.deckbuilder.internal.analytics.Event

class CrashlyticsAnalyticInterface : AnalyticInterface {

    override fun setUserId(id: String) {
        FirebaseCrashlytics.getInstance()
            .setUserId(id)
    }

    override fun setUserProperty(key: String, value: String?) {
        if (value != null) {
            FirebaseCrashlytics.getInstance()
                .setCustomKey(key, value)
        }
    }

    override fun postEvent(event: Event) {
    }
}
