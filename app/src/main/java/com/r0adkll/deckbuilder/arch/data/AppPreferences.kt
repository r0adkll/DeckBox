package com.r0adkll.deckbuilder.arch.data

import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.ftinc.kit.kotlin.extensions.Preferences
import com.ftinc.kit.kotlin.extensions.Preferences.*
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.Expansion
import com.r0adkll.deckbuilder.arch.domain.features.tournament.model.AgeDivision
import com.r0adkll.deckbuilder.util.extensions.RxPreferences
import com.r0adkll.deckbuilder.util.extensions.RxPreferences.*
import javax.inject.Inject


class AppPreferences @Inject constructor(
        override val sharedPreferences: SharedPreferences,
        override val rxSharedPreferences: RxSharedPreferences
) : Preferences, RxPreferences{

    companion object {
        const val KEY_ONBOARDING = "pref_onboarding"
        const val KEY_QUICKSTART = "pref_quickstart"
        const val KEY_EXPANSIONS = "pref_expansions"
        const val KEY_EXPANSIONS_TIMESTAMP = "pref_expansions_timestamp"
        const val KEY_DEFAULT_ENERGY_SET = "pref_default_energy_set"
        const val KEY_PLAYER_NAME = "pref_player_name"
        const val KEY_PLAYER_ID = "pref_player_id"
        const val KEY_PLAYER_AGE_DIVISION = "pref_player_age_division"
        const val KEY_PLAYER_DOB = "pref_player_dob"

        const val KEY_LAST_VERSION = "pref_last_version"
        const val KEY_SET_ULTRAPRISM = "pref_set_ultraprism"
    }


    var onboarding by BooleanPreference(KEY_ONBOARDING, false)
    var quickStart by BooleanPreference(KEY_QUICKSTART, true)
    var lastVersion by IntPreference(KEY_LAST_VERSION, -1)
    val previewUltraPrism by ReactiveBooleanPreference(KEY_SET_ULTRAPRISM, true)

    val expansions by ReactiveExpansionsPreference(KEY_EXPANSIONS)
    var expansionsTimestamp by LongPreference(KEY_EXPANSIONS_TIMESTAMP, 0L)

    val basicEnergySet by ReactiveBasicEnergySetPreference(KEY_DEFAULT_ENERGY_SET)

    val playerName by ReactiveStringPreference(KEY_PLAYER_NAME)
    val playerId by ReactiveStringPreference(KEY_PLAYER_ID)
    val playerDOB by ReactiveDatePreference(KEY_PLAYER_DOB)
    val playerAgeDivision by ReactiveEnumPreference(KEY_PLAYER_AGE_DIVISION, AgeDivision.MASTERS)


    fun clear() {
        sharedPreferences.edit()
                .clear()
                .apply()
    }
}