package com.r0adkll.deckbuilder.arch.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.r0adkll.deckbuilder.arch.ui.features.home.HomeActivity
import com.r0adkll.deckbuilder.arch.ui.features.onboarding.OnboardingActivity


class MainActivity : AppCompatActivity() {

    private val firebase: FirebaseAuth by lazy { FirebaseAuth.getInstance() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (firebase.currentUser != null) {
            startActivity(HomeActivity.createIntent(this))
        }
        else {
            startActivity(OnboardingActivity.createIntent(this))
        }

        finish()
    }
}