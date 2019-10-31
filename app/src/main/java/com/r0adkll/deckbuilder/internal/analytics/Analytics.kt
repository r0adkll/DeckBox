package com.r0adkll.deckbuilder.internal.analytics

/**
 * The main analytic interfacing class. Make all analytic calls in the UI/Codebase via this class
 */
object Analytics {

    private val analyticInterfaces: ArrayList<AnalyticInterface> = ArrayList()

    fun add(analytic: AnalyticInterface) {
        analyticInterfaces += analytic
    }

    fun remove(analytic: AnalyticInterface) {
        analyticInterfaces -= analytic
    }

    fun userId(id: String) {
        analyticInterfaces.forEach { it.setUserId(id) }
    }

    fun userProperty(key: String, value: String?) {
        analyticInterfaces.forEach { it.setUserProperty(key, value) }
    }

    fun event(event: Event) {
        analyticInterfaces.forEach { it.postEvent(event) }
    }
}
