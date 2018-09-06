package com.r0adkll.deckbuilder.arch.data.features.expansions.cache


import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.Remote
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import io.reactivex.Observable


class PreferenceExpansionCache(
        val preferences: AppPreferences,
        val remote: Remote
) : ExpansionCache {

    override fun putExpansions(expansions: List<Expansion>) {
        preferences.expansions.set(ArrayList(expansions))
    }


    override fun getExpansions(): Observable<List<Expansion>> {
        // Check to see if we have the latest expansion as per the remote config
        if (preferences.expansions.get().none { it.code == remote.expansionVersion?.expansionCode }) {
            preferences.expansions.delete()
        }

        return preferences.expansions.asObservable()
                .take(1)
    }


    override fun clear() {
        preferences.expansions.delete()
    }
}