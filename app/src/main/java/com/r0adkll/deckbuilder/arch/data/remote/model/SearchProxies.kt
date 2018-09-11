package com.r0adkll.deckbuilder.arch.data.remote.model



data class SearchProxies(private val proxies: List<Proxy>) {

    fun apply(query: String): String {
        var newQuery = query

        proxies.forEach { p ->
            val regex = Regex(p.regex, RegexOption.IGNORE_CASE)
            newQuery = regex.replace(newQuery, p.replacement)
        }

        return newQuery
    }

    data class Proxy(
            val regex: String,
            val replacement: String
    )
}