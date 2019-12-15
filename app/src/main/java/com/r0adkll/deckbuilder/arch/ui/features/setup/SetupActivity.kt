package com.r0adkll.deckbuilder.arch.ui.features.setup

import android.content.Context
import android.content.Intent
import android.graphics.Shader
import android.os.Bundle
import android.view.WindowManager
import com.ftinc.kit.arch.presentation.BaseActivity
import com.ftinc.kit.extensions.color
import com.ftinc.kit.extensions.drawable
import com.ftinc.kit.extensions.snackbar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.ui.InternalWebBrowser
import com.r0adkll.deckbuilder.arch.ui.features.home.HomeActivity
import com.r0adkll.deckbuilder.arch.ui.widgets.TileDrawable
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import kotlinx.android.synthetic.main.activity_setup.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class SetupActivity : BaseActivity() {

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var googleSignInClient: GoogleSignInClient

    @Inject lateinit var preferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        val tileDrawable = drawable(R.drawable.ic_app_pattern)!!
        pattern.setImageDrawable(TileDrawable(tileDrawable, Shader.TileMode.REPEAT, PATTERN_ANGLE))

        setupClient()

        actionSignIn.setOnClickListener {
            signIn()
        }

        actionContinue.setOnClickListener {
            signInOffline()
        }

        cardSwitcher?.let {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        cardSwitcher?.setOnPaletteChangeListener {
            val primaryColor = it.vibrantSwatch
            val primaryDarkColor = it.darkVibrantSwatch

            setupRoot?.setBackgroundColor(primaryColor?.rgb ?: color(R.color.primaryColor))
            window.statusBarColor = primaryDarkColor?.rgb ?: color(R.color.primaryDarkColor)
            setupTitle?.setTextColor(primaryColor?.titleTextColor ?: color(R.color.white))
            setupSubtitle?.setTextColor(primaryColor?.bodyTextColor ?: color(R.color.white))
            actionContinue.setTextColor(primaryColor?.titleTextColor ?: color(R.color.white))
        }

        actionPrivacyPolicy.setOnClickListener {
            InternalWebBrowser.show(this, R.string.pref_about_privacy_policy, getString(R.string.privacy_policy_url))
        }
    }

    override fun setupComponent() {
        DeckApp.component.inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_SIGN_IN -> {
                val result = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(result)
            }
        }
    }

    private fun setupClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signInOffline() {
        preferences.offlineId.set(UUID.randomUUID().toString())
        Analytics.event(Event.Login.Offline)
        startActivity(HomeActivity.createIntent(this@SetupActivity))
        finish()
    }

    private fun handleSignInResult(completionTask: Task<GoogleSignInAccount>) {
        try {
            val account = completionTask.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuth.signInWithCredential(credential)
                .addOnFailureListener {
                    Timber.e(it, "Some critical error occurred when trying to sign in")
                    snackbar("Uh-oh! Something happened, unable to sign-in")
                }
                .addOnCompleteListener { r ->
                    if (r.isSuccessful) {
                        // Auto-grab the user's name from their account
                        r.result?.user?.displayName?.let {
                            preferences.playerName.set(it)
                        }
                        r.result?.user?.uid?.let {
                            Analytics.userId(it)
                        }
                        Analytics.event(Event.Login.Google)
                        startActivity(HomeActivity.createIntent(this@SetupActivity))
                        finish()
                    } else {
                        Timber.e(r.exception, "Authentication Failed: ${r.result}")
                        snackbar("Authentication failed")
                    }
                }
        } catch (e: ApiException) {
            Timber.e("Authentication Failed: ${e.message}")
            snackbar("Authenticated failed")
        }
    }

    companion object {
        private const val PATTERN_ANGLE = -45f
        const val RC_SIGN_IN = 100

        fun createIntent(context: Context): Intent = Intent(context, SetupActivity::class.java)
    }
}
