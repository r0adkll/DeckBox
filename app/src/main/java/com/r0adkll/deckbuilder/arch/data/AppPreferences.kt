package com.r0adkll.deckbuilder.arch.data

import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.ftinc.kit.kotlin.extensions.Preferences
import com.ftinc.kit.kotlin.extensions.Preferences.BooleanPreference
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.util.extensions.RxPreferences
import javax.inject.Inject


class AppPreferences @Inject constructor(
        override val sharedPreferences: SharedPreferences,
        override val rxSharedPreferences: RxSharedPreferences
) : Preferences, RxPreferences{

    companion object {
        const val KEY_ONBOARDING = "pref_onboarding"
        const val KEY_EXPANSIONS = "pref_expansions"
        const val KEY_EXPANSIONS_TIMESTAMP = "pref_expansions_timestamp"
    }


    var onboarding by BooleanPreference(KEY_ONBOARDING, false)

    val expansions by RxPreferences.ReactiveJsonPreference<List<Expansion>>(KEY_EXPANSIONS, emptyList())
    var expansionsTimestamp by Preferences.LongPreference(KEY_EXPANSIONS_TIMESTAMP, 0L)


    fun clear() {
        sharedPreferences.edit()
                .clear()
                .apply()
    }
}