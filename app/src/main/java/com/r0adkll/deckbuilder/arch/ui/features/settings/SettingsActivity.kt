package com.r0adkll.deckbuilder.arch.ui.features.settings


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v14.preference.PreferenceFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.preference.Preference
import com.ftinc.kit.kotlin.extensions.clear
import com.ftinc.kit.util.IntentUtils
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.missingcards.MissingCardsActivity
import com.r0adkll.deckbuilder.arch.ui.features.setup.SetupActivity
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.extensions.snackbar
import timber.log.Timber
import javax.inject.Inject


class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appbar?.setNavigationOnClickListener { finish() }
    }


    override fun setupComponent(component: AppComponent) {
    }



    class SettingsFragment : PreferenceFragment(), GoogleApiClient.OnConnectionFailedListener {

        private val RC_SIGN_IN = 100
        private var googleClient: GoogleApiClient? = null

        @Inject lateinit var preferences: AppPreferences


        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            setupClient()
        }


        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == RC_SIGN_IN) {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                handleSignInResult(result)
            }
        }


        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            DeckApp.component.inject(this)
            addPreferencesFromResource(R.xml.settings_preferences)
            setupPreferences()
        }


        override fun onPreferenceTreeClick(preference: Preference): Boolean {
            return when(preference.key) {
                "pref_account_profile" -> {
                    true
                }
                "pref_about_developer" -> {
                    startActivity(IntentUtils.openLink(getString(R.string.developer_website_url)))
                    true
                }
                "pref_about_oss" -> {
                    val intent = Intent(activity, OssLicensesMenuActivity::class.java)
                    val title = getString(R.string.activity_licenses)
                    intent.putExtra("title", title)
                    startActivity(intent)
                    true
                }
                "pref_help_feedback" -> {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    val text =
                            "Version: ${BuildConfig.VERSION_NAME} \n" +
                            "UserId: $userId \n\n\n"
                    val emailAddress = getString(R.string.support_email_address)
                    val intent = IntentUtils.sendEmail(emailAddress, "Feedback", text)
                    startActivity(intent)
                    true
                }
                "pref_missing_card" -> {
                    MissingCardsActivity.show(activity)
                    true
                }
                "pref_account_link" -> {
                    signIn()
                    true
                }
                "pref_account_signout" -> {
                    preferences.deviceId = null
                    FirebaseAuth.getInstance().signOut()
                    googleClient?.clearDefaultAccountAndReconnect()
                    val intent = SetupActivity.createIntent(activity).clear()
                    startActivity(intent)
                    activity.finish()
                    true
                }
                else -> super.onPreferenceTreeClick(preference)
            }
        }


        override fun onConnectionFailed(p0: ConnectionResult) {
            Timber.e("onConnectionFailed(${p0.errorCode} :: ${p0.errorMessage}")
        }


        private fun setupPreferences() {
            val profilePref = findPreference("pref_account_profile") as ProfilePreference
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                profilePref.avatarUrl = user.photoUrl

                profilePref.title = if (user.isAnonymous) {
                    getString(R.string.user_anonymous_title)
                } else {
                    user.displayName
                }

                profilePref.summary = if (user.isAnonymous) {
                    user.uid
                } else {
                    user.email
                }

                val linkAccount = findPreference("pref_account_link")
                linkAccount.isVisible = user.isAnonymous
            } else {
                profilePref.title = "Offline"
                profilePref.summary = preferences.deviceId

                val linkAccount = findPreference("pref_account_link")
                linkAccount.isVisible = false
            }

            val versionPref = findPreference("pref_about_version")
            versionPref.summary = BuildConfig.VERSION_NAME
        }


        private fun setupClient() {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

            googleClient = GoogleApiClient.Builder(activity)
                    .enableAutoManage(activity as FragmentActivity, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build()
        }


        private fun signIn() {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleClient)
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }


        private fun handleSignInResult(result: GoogleSignInResult) {
            if (result.isSuccess) {
                val acct = result.signInAccount
                val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
                FirebaseAuth.getInstance()
                        .currentUser?.linkWithCredential(credential)
                        ?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                snackbar("${it.result.user.email} linked!")
                                setupPreferences()
                            }
                            else {
                                Timber.e(it.exception, "Unable to link account")
                                snackbar(it.exception?.localizedMessage ?: "Unable to link account", Snackbar.LENGTH_LONG)
                            }
                        }
            }
            else {
                Timber.e("Unable to link account: ${result.status}")
                snackbar("Unable to link account.")
            }
        }
    }


    companion object {

        fun createIntent(context: Context): Intent = Intent(context, SettingsActivity::class.java)
    }
}