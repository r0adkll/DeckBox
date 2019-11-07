package com.r0adkll.deckbuilder.util

import com.crashlytics.android.Crashlytics
import com.r0adkll.deckbuilder.internal.analytics.AnalyticInterface
import com.r0adkll.deckbuilder.internal.analytics.Event

class CrashlyticsAnalyticInterface : AnalyticInterface {

    override fun setUserId(id: String) {
        Crashlytics.setUserIdentifier(id)
    }

    override fun setUserProperty(key: String, value: String?) {
        Crashlytics.setString(key, value)
    }

    override fun postEvent(event: Event) {
    }
}
