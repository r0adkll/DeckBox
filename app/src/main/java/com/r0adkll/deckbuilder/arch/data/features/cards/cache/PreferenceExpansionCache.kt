package com.r0adkll.deckbuilder.arch.data.features.cards.cache


import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import io.reactivex.Observable


class PreferenceExpansionCache(
        val preferences: AppPreferences
) : ExpansionCache {

    override fun putExpansions(expansions: List<Expansion>) {
        preferences.expansions.set(ArrayList(expansions))
        preferences.expansionsTimestamp = System.currentTimeMillis()
    }


    override fun getExpansions(): Observable<List<Expansion>> {
        return preferences.expansions.asObservable()
                .map { it.toList() }
                .take(1)
    }


    override fun clear() {
        preferences.expansions.delete()
    }
}