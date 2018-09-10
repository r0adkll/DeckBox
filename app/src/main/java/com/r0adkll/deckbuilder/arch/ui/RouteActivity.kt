package com.r0adkll.deckbuilder.arch.ui

import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.remote.Remote
import com.r0adkll.deckbuilder.arch.domain.features.editing.repository.EditRepository
import com.r0adkll.deckbuilder.arch.ui.features.deckbuilder.DeckBuilderActivity
import com.r0adkll.deckbuilder.arch.ui.features.home.HomeActivity
import com.r0adkll.deckbuilder.arch.ui.features.onboarding.OnboardingActivity
import com.r0adkll.deckbuilder.arch.ui.features.setup.SetupActivity
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject


class RouteActivity : AppCompatActivity() {

    private val firebase: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    @Inject lateinit var preferences: AppPreferences
    @Inject lateinit var remote: Remote
    @Inject lateinit var editor: EditRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DeckApp.component.inject(this)

        compatibilityCheck()
        remote.check()

        var shouldFinish = true
        if (firebase.currentUser != null || preferences.deviceId != null) {
            firebase.currentUser?.uid?.let { Analytics.userId(it) }

            val action = intent?.action
            when(action) {
                ACTION_NEW_DECK -> {
                    shouldFinish = false
                    editor.createSession()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ sessionId ->
                                TaskStackBuilder.create(this)
                                        .addParentStack(HomeActivity::class.java)
                                        .addNextIntent(DeckBuilderActivity.createIntent(this, sessionId))
                                        .startActivities()
                                finish()
                            }, {
                                startActivity(HomeActivity.createIntent(this))
                                finish()
                            })
                }
                else -> startActivity(HomeActivity.createIntent(this))
            }
        }
        else {
            if (preferences.onboarding) {
                startActivity(SetupActivity.createIntent(this))
            } else {
                startActivity(OnboardingActivity.createIntent(this))
            }
        }

        if (shouldFinish) {
            finish()
        }
    }


    private fun compatibilityCheck() {
        if (preferences.lastVersion == -1 && preferences.onboarding) {
            // We want to disable the quickstart if the user has already passed the onboarding phase
            preferences.quickStart = false
        }

        // Set the last version that was installed for future compat checks
        preferences.lastVersion = BuildConfig.VERSION_CODE
    }


    companion object {
        const val ACTION_NEW_DECK = "com.r0adkll.deckbuilder.intent.ACTION_NEW_DECK"
        const val ACTION_OPEN_DECK = "com.r0adkll.deckbuilder.intent.ACTION_OPEN_DECK"
        const val EXTRA_DECK_ID = "com.r0adkll.deckbuilder.intent.EXTRA_DECK_ID"


        fun createIntent(context: Context): Intent {
            return Intent(context, RouteActivity::class.java)
        }


        fun createNewDeckIntent(context: Context): Intent {
            val intent = createIntent(context)
            intent.action = ACTION_NEW_DECK
            return intent
        }


        fun createOpenDeckIntent(context: Context, deckId: String): Intent {
            val intent = createIntent(context)
            intent.action = ACTION_OPEN_DECK
            intent.putExtra(EXTRA_DECK_ID, deckId)
            return intent
        }
    }
}