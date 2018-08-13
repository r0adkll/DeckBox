package com.r0adkll.deckbuilder.arch.data


import android.content.Context
import android.content.SharedPreferences
import com.ftinc.kit.kotlin.extensions.Preferences
import com.ftinc.kit.kotlin.extensions.Preferences.BooleanPreference
import javax.inject.Inject


/**
 * Preference tracker for managing one-off per-user flags for such things as new feature badges,
 * and special user onboarding
 */
class FlagPreferences @Inject constructor(
        context: Context
) : Preferences {

    override val sharedPreferences: SharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)


    var newFeatureDeckImage by BooleanPreference(KEY_NEWFEATURE_DECK_IMAGE, false) // feature has been out long enough


    companion object {
        private const val NAME = "com.r0adkll.deckbuilder.FlagPreferences"

        private const val KEY_NEWFEATURE_DECK_IMAGE = "key_newfeature_deck_image"
    }
}