package com.r0adkll.deckbuilder.arch.data

import android.content.Context
import android.content.SharedPreferences
import com.ftinc.kit.kotlin.extensions.Preferences
import javax.inject.Inject

/**
 * Preference tracker for managing one-off per-user flags for such things as new feature badges,
 * and special user onboarding
 */
class FlagPreferences @Inject constructor(
        context: Context
) : Preferences {

    override val sharedPreferences: SharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    companion object {
        private const val NAME = "com.r0adkll.deckbuilder.FlagPreferences"
    }
}
