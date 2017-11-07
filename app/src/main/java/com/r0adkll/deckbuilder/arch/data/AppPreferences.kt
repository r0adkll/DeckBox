package com.r0adkll.deckbuilder.arch.data

import android.content.SharedPreferences
import com.ftinc.kit.kotlin.extensions.Preferences
import com.ftinc.kit.kotlin.extensions.Preferences.BooleanPreference
import javax.inject.Inject


class AppPreferences @Inject constructor(
        override val sharedPreferences: SharedPreferences
) : Preferences {

    companion object {
        const val KEY_ONBOARDING = "pref_onboarding"
    }


    var onboarding by BooleanPreference(KEY_ONBOARDING, false)


    fun clear() {
        sharedPreferences.edit()
                .clear()
                .apply()
    }
}