package com.r0adkll.deckbuilder.arch.data

import android.content.SharedPreferences
import com.ftinc.kit.kotlin.extensions.Preferences
import javax.inject.Inject


class AppPreferences @Inject constructor(
        override val sharedPreferences: SharedPreferences
) : Preferences {



}