package com.r0adkll.deckbuilder.arch.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.ui.features.home.HomeActivity
import com.r0adkll.deckbuilder.arch.ui.features.onboarding.OnboardingActivity
import com.r0adkll.deckbuilder.arch.ui.features.setup.SetupActivity
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private val firebase: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    @Inject lateinit var preferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DeckApp.component.inject(this)

        if (firebase.currentUser != null) {
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
}