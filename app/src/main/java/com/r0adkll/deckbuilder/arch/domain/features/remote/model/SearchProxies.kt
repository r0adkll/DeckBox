package com.r0adkll.deckbuilder.arch.domain.features.remote.model

import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event

data class SearchProxies(private val proxies: List<Proxy>) {

    fun apply(query: String): String {
        var newQuery = query

        proxies.forEach { p ->
            val regex = Regex(p.regex, RegexOption.IGNORE_CASE)
            newQuery = regex.replace(newQuery, p.replacement)

            // Detect use of search proxies and log them in the analytics
            if (newQuery != query) {
                Analytics.event(Event.SearchProxy(p))
            }
        }

        return newQuery
    }

    data class Proxy(
        val regex: String,
        val replacement: String
    )
}
