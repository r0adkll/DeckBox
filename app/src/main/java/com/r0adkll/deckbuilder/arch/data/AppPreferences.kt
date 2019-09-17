package com.r0adkll.deckbuilder.arch.data

import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.f2prateek.rx.preferences2.Preference as RxPreference
import com.ftinc.kit.kotlin.extensions.Preferences
import com.ftinc.kit.kotlin.extensions.Preferences.*
import com.r0adkll.deckbuilder.arch.domain.features.exporter.tournament.model.AgeDivision
import com.r0adkll.deckbuilder.util.extensions.RxPreferences
import com.r0adkll.deckbuilder.util.extensions.RxPreferences.*
import javax.inject.Inject


open class AppPreferences @Inject constructor(
        override val sharedPreferences: SharedPreferences,
        override val rxSharedPreferences: RxSharedPreferences
) : Preferences, RxPreferences{

    companion object {
        const val KEY_ONBOARDING = "pref_onboarding"
        const val KEY_QUICKSTART = "pref_quickstart"
        const val KEY_EXPANSIONS = "pref_expansions_sm7" // Bump name to HARD force people to the new expansion
        const val KEY_PREVIEW_EXPANSIONS = "pref_preview_expansions"
        const val KEY_EXPANSIONS_VERSION = "pref_expansions_version"
        const val KEY_PREVIEW_EXPANSIONS_VERSION = "pref_preview_expansions_version"
        const val KEY_DEFAULT_ENERGY_SET = "pref_default_energy_set"
        const val KEY_PLAYER_NAME = "pref_player_name"
        const val KEY_PLAYER_ID = "pref_player_id"
        const val KEY_OFFLINE_ID = "pref_offline_id"
        const val KEY_OFFLINE_EXPANSIONS = "pref_offline_expansions"
        const val KEY_OFFLINE_OUTLINE = "key_offline_outline"
        const val KEY_PLAYER_AGE_DIVISION = "pref_player_age_division"
        const val KEY_PLAYER_DOB = "pref_player_dob"
        const val KEY_PREVIEW_VERSION = "pref_last_preview_version"
        const val KEY_COLLECTION_MIGRATION = "pref_collection_migration"
        const val KEY_LAST_VERSION = "pref_last_version"
        const val KEY_DEVICE_ID = "pref_local_offline_device_id"
        const val KEY_TEST_USER_ID = "pref_developer_test_user_id"
    }

    open var testUserId by StringPreference(KEY_TEST_USER_ID)

    open var onboarding by BooleanPreference(KEY_ONBOARDING, false)
    open var lastVersion by IntPreference(KEY_LAST_VERSION, -1)
    open var deviceId by StringPreference(KEY_DEVICE_ID)
    open var expansionsVersion by IntPreference(KEY_EXPANSIONS_VERSION, 1)
    open var previewExpansionsVersion by IntPreference(KEY_PREVIEW_EXPANSIONS_VERSION, 1)

    open val offlineId by ReactiveStringPreference(KEY_OFFLINE_ID)
    open val offlineExpansions by ReactiveStringSetPreference(KEY_OFFLINE_EXPANSIONS)
    open val offlineOutline by ReactiveBooleanPreference(KEY_OFFLINE_OUTLINE, true)
    open val quickStart by ReactiveBooleanPreference(KEY_QUICKSTART, true)
    open val showCollectionMigration by ReactiveBooleanPreference(KEY_COLLECTION_MIGRATION, true)
    open val expansions by ReactiveExpansionsPreference(KEY_EXPANSIONS)
    open val previewExpansions by ReactiveExpansionsPreference(KEY_PREVIEW_EXPANSIONS)
    open val basicEnergySet by ReactiveBasicEnergySetPreference(KEY_DEFAULT_ENERGY_SET)

    open val playerName by ReactiveStringPreference(KEY_PLAYER_NAME)
    open val playerId by ReactiveStringPreference(KEY_PLAYER_ID)
    open val playerDOB by ReactiveDatePreference(KEY_PLAYER_DOB)
    open val playerAgeDivision by ReactiveEnumPreference(KEY_PLAYER_AGE_DIVISION, AgeDivision.MASTERS)

    open val previewVersion by ReactiveIntPreference(KEY_PREVIEW_VERSION)

    fun clear() {
        sharedPreferences.edit()
                .clear()
                .apply()
    }
}
