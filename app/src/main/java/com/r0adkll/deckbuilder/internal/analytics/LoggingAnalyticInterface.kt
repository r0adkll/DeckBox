package com.r0adkll.deckbuilder.internal.analytics


import timber.log.Timber


class LoggingAnalyticInterface : AnalyticInterface {

    override fun setUserId(id: String) {
        Timber.i("Analytics::setUserId($id)")
    }

    override fun setUserProperty(key: String, value: String?) {
        Timber.i("Analytics::setUserProperty($key, $value)")
    }

    override fun postEvent(event: Event) {
        Timber.i("Analytics::postEvent($event)")
    }
}