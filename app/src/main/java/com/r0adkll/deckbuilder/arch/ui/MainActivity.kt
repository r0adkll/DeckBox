package com.r0adkll.deckbuilder.arch.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.ui.features.home.HomeActivity
import com.r0adkll.deckbuilder.arch.ui.features.onboarding.OnboardingActivity
import com.r0adkll.deckbuilder.arch.ui.features.setup.SetupActivity
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private val firebase: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    @Inject lateinit var preferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DeckApp.component.inject(this)

        compatibilityCheck()

        if (firebase.currentUser != null || preferences.deviceId != null) {
            firebase.currentUser?.uid?.let { Analytics.userId(it) }
            startActivity(HomeActivity.createIntent(this))
        }
        else {
            if (preferences.onboarding) {
                startActivity(SetupActivity.createIntent(this))
            } else {
                startActivity(OnboardingActivity.createIntent(this))
            }
        }

        finish()
    }


    private fun compatibilityCheck() {
        if (preferences.lastVersion == -1 && preferences.onboarding) {
            // We want to disable the quickstart if the user has already passed the onboarding phase
            preferences.quickStart = false
        }

        // Set the last version that was installed for future compat checks
        preferences.lastVersion = BuildConfig.VERSION_CODE
    }
}