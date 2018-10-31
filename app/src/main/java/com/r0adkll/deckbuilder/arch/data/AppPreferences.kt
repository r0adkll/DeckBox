package com.r0adkll.deckbuilder.arch.data

import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.ftinc.kit.kotlin.extensions.Preferences
import com.ftinc.kit.kotlin.extensions.Preferences.*
import com.r0adkll.deckbuilder.arch.domain.features.exporter.tournament.model.AgeDivision
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
        const val KEY_EXPANSIONS = "pref_expansions_sm7" // Bump name to HARD force people to the new expansion
        const val KEY_EXPANSIONS_VERSION = "pref_expansions_version"
        const val KEY_DEFAULT_ENERGY_SET = "pref_default_energy_set"
        const val KEY_PLAYER_NAME = "pref_player_name"
        const val KEY_PLAYER_ID = "pref_player_id"
        const val KEY_OFFLINE_ID = "pref_offline_id"
        const val KEY_OFFLINE_EXPANSIONS = "pref_offline_expansions"
        const val KEY_PLAYER_AGE_DIVISION = "pref_player_age_division"
        const val KEY_PLAYER_DOB = "pref_player_dob"
        const val KEY_PREVIEW_VERSION = "pref_last_preview_version"

        const val KEY_LAST_VERSION = "pref_last_version"
        const val KEY_DEVICE_ID = "pref_local_offline_device_id"
    }


    var onboarding by BooleanPreference(KEY_ONBOARDING, false)
    var lastVersion by IntPreference(KEY_LAST_VERSION, -1)
    var deviceId by StringPreference(KEY_DEVICE_ID)
    var expansionsVersion by IntPreference(KEY_EXPANSIONS_VERSION, 1)

    val offlineId by ReactiveStringPreference(KEY_OFFLINE_ID)
    val offlineExpansions by ReactiveStringSetPreference(KEY_OFFLINE_EXPANSIONS)
    val quickStart by ReactiveBooleanPreference(KEY_QUICKSTART, true)
    val expansions by ReactiveExpansionsPreference(KEY_EXPANSIONS)
    val basicEnergySet by ReactiveBasicEnergySetPreference(KEY_DEFAULT_ENERGY_SET)

    val playerName by ReactiveStringPreference(KEY_PLAYER_NAME)
    val playerId by ReactiveStringPreference(KEY_PLAYER_ID)
    val playerDOB by ReactiveDatePreference(KEY_PLAYER_DOB)
    val playerAgeDivision by ReactiveEnumPreference(KEY_PLAYER_AGE_DIVISION, AgeDivision.MASTERS)

    val previewVersion by ReactiveIntPreference(KEY_PREVIEW_VERSION)

    fun clear() {
        sharedPreferences.edit()
                .clear()
                .apply()
    }
}