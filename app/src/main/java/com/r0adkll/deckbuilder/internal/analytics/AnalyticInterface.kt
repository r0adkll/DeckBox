package com.r0adkll.deckbuilder.internal.analytics

interface AnalyticInterface {

    fun setUserId(id: String)
    fun setUserProperty(key: String, value: String?)
    fun postEvent(event: Event)
}
