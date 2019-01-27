package com.r0adkll.deckbuilder.arch.ui.features.setup

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.core.content.IntentCompat
import com.ftinc.kit.kotlin.extensions.color
import com.ftinc.kit.util.IntentUtils
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.home.HomeActivity
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.RxFirebase
import com.r0adkll.deckbuilder.util.extensions.plusAssign
import com.r0adkll.deckbuilder.util.extensions.snackbar
import kotlinx.android.synthetic.main.activity_setup.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class SetupActivity : BaseActivity(), GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private var googleClient: GoogleApiClient? = null

    @Inject lateinit var preferences: AppPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)
        setupClient()

        action_signin.isEnabled = false
        action_signin.setOnClickListener {
            signIn()
        }

        action_continue.setOnClickListener {
            signInAnonymously()
        }

        cardSwitcher?.let {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        cardSwitcher?.setOnPaletteChangeListener {
            val primaryColor = it.dominantSwatch
            val primaryDarkColor = it.darkVibrantSwatch

            setupRoot?.setBackgroundColor(primaryColor?.rgb ?: color(R.color.primaryColor))
            window.statusBarColor = primaryDarkColor?.rgb ?: color(R.color.primaryDarkColor)
            setupTitle?.setTextColor(primaryColor?.titleTextColor ?: color(R.color.white))
            setupSubtitle?.setTextColor(primaryColor?.bodyTextColor ?: color(R.color.white))
            action_continue.setTextColor(primaryColor?.titleTextColor ?: color(R.color.white))
        }

        actionPrivacyPolicy.setOnClickListener {
            val intent = IntentUtils.openLink(getString(R.string.privacy_policy_url))
            if (IntentUtils.isIntentAvailable(this, intent)) {
                startActivity(intent)
            } else {
                Intent.createChooser(intent, getString(R.string.intent_chooser_link_title)).apply {
                    startActivity(this)
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            RC_SIGN_IN -> {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                handleSignInResult(result)
            }
            RC_PLAY_SERVICES_ERROR -> {
                setupClient()
            }
        }
    }


    override fun setupComponent(component: AppComponent) {
        component.inject(this)
    }


    override fun onConnectionFailed(p0: ConnectionResult) {
        Timber.e("onConnectionFailed(${p0.errorCode} :: ${p0.errorMessage}")
        p0.startResolutionForResult(this, RC_PLAY_SERVICES_ERROR)
    }


    override fun onConnected(p0: Bundle?) {
        Timber.i("onConnected($p0)")
        action_signin.isEnabled = true
    }


    override fun onConnectionSuspended(p0: Int) {
        Timber.i("onConnectionSuspended($p0)")
    }


    private fun setupClient() {
        val result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        if (result == ConnectionResult.SUCCESS) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

            googleClient = GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addConnectionCallbacks(this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build()
        } else {
            GoogleApiAvailability.getInstance().showErrorDialogFragment(this, result, RC_PLAY_SERVICES_ERROR)
        }
    }


    private fun signIn() {
        if (googleClient?.isConnected == true) {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleClient)
            startActivityForResult(signInIntent, RC_SIGN_IN)
        } else if (googleClient != null && googleClient?.isConnecting != true) {
            // We should probably attempt to re-setup the client here
            setupClient()
        }
    }


    private fun signInAnonymously() {
//        try {
//            disposables += RxFirebase.from(firebaseAuth.signInAnonymously())
//                    .subscribe({
//                        Analytics.event(Event.Login.Anonymous)
//                        startActivity(HomeActivity.createIntent(this@SetupActivity))
//                        finish()
//                    }, {
//                        Timber.e(it)
//                        Timber.i("Anonymous signin failed, generate an offline device id")
//                        signInOffline()
//                    })
//        } catch (e: Exception) {
//            Timber.e(e)
//            Timber.i("Anonymous signin failed, generate an offline device id")
//            signInOffline()
//        }

        signInOffline()
    }


    private fun signInOffline() {
        preferences.offlineId.set(UUID.randomUUID().toString())
        Analytics.event(Event.Login.Offline)
        startActivity(HomeActivity.createIntent(this@SetupActivity))
        finish()
    }


    private fun handleSignInResult(result: GoogleSignInResult) {
        try {
            if (result.isSuccess) {
                val acct = result.signInAccount
                val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
                firebaseAuth.signInWithCredential(credential)
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
                                snackbar("Authentication failed")
                            }
                        }
            } else {
                snackbar("Authenticated failed")
            }
        } catch (e: Exception) {
            Timber.e(e)
            snackbar("Unable to sign in with your Google account")
        }
    }


    companion object {
        const val RC_SIGN_IN = 100
        const val RC_PLAY_SERVICES_ERROR = 10

        fun createIntent(context: Context): Intent = Intent(context, SetupActivity::class.java)
    }
}