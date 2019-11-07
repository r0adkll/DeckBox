package com.r0adkll.deckbuilder.arch.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import com.r0adkll.deckbuilder.arch.ui.features.home.HomeActivity
import com.r0adkll.deckbuilder.arch.ui.features.setup.SetupActivity
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.UserProperty.LEVEL
import com.r0adkll.deckbuilder.internal.analytics.UserProperty.LEVEL_GOOGLE
import com.r0adkll.deckbuilder.internal.analytics.UserProperty.LEVEL_LOCAL
import com.r0adkll.deckbuilder.internal.analytics.UserProperty.LEVEL_OFFLINE_LEGACY
import javax.inject.Inject

class RouteActivity : AppCompatActivity() {

    private val firebase: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    @Inject lateinit var preferences: AppPreferences
    @Inject lateinit var remote: Remote

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DeckApp.component.inject(this)

        compatibilityCheck()
        remote.check()

        if (isSignedIn()) {
            setUserLevelProperty()

            firebase.currentUser?.uid?.let {
                Analytics.userId(it)
            }

            startActivity(HomeActivity.createIntent(this))
        } else {
            Shortcuts.clearShortcuts(this)
            startActivity(SetupActivity.createIntent(this))
        }

        finish()
    }

    private fun isSignedIn(): Boolean {
        return firebase.currentUser != null ||
            !preferences.deviceId.isNullOrBlank() ||
            (preferences.offlineId.isSet && preferences.offlineId.get().isNotBlank())
    }

    private fun setUserLevelProperty() {
        when {
            firebase.currentUser != null ->
                Analytics.userProperty(LEVEL, LEVEL_GOOGLE)
            !preferences.deviceId.isNullOrBlank() ->
                Analytics.userProperty(LEVEL, LEVEL_LOCAL)
            (preferences.offlineId.isSet && preferences.offlineId.get().isNotBlank()) ->
                Analytics.userProperty(LEVEL, LEVEL_OFFLINE_LEGACY)
        }
    }

    private fun compatibilityCheck() {
        if (preferences.lastVersion == -1 && preferences.onboarding) {
            // We want to disable the quickstart if the user has already passed the onboarding phase
            preferences.quickStart.set(false)
        }

        // Set the last version that was installed for future compat checks
        preferences.lastVersion = BuildConfig.VERSION_CODE
    }

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, RouteActivity::class.java)
        }
    }
}
