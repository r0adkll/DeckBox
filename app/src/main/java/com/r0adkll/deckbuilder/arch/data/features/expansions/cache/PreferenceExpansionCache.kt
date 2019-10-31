package com.r0adkll.deckbuilder.arch.data.features.expansions.cache

import com.f2prateek.rx.preferences2.Preference
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.domain.features.expansions.model.Expansion
import io.reactivex.Observable

class PreferenceExpansionCache(
        val preferences: AppPreferences,
        private val selector: AppPreferences.() -> Preference<List<Expansion>>
) : ExpansionCache {

    override fun putExpansions(expansions: List<Expansion>) {
        preferences.selector().set(ArrayList(expansions))
    }

    override fun getExpansions(): Observable<List<Expansion>> {
        return preferences.selector().asObservable()
                .take(1)
    }

    override fun clear() {
        preferences.selector().delete()
    }
}
