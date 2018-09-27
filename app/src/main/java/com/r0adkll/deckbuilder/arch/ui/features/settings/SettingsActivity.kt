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
import com.r0adkll.deckbuilder.arch.data.features.cards.service.CacheService
import com.r0adkll.deckbuilder.arch.domain.features.cards.CacheManager
import com.r0adkll.deckbuilder.arch.ui.Shortcuts
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.missingcards.MissingCardsActivity
import com.r0adkll.deckbuilder.arch.ui.features.setup.SetupActivity
import com.r0adkll.deckbuilder.internal.di.AppComponent
import com.r0adkll.deckbuilder.util.extensions.snackbar
import io.reactivex.disposables.CompositeDisposable
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

        private var googleClient: GoogleApiClient? = null

        @Inject lateinit var preferences: AppPreferences
        @Inject lateinit var cacheManager: CacheManager
        private val disposables = CompositeDisposable()


        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            setupClient()
        }


        override fun onDestroy() {
            super.onDestroy()
            disposables.clear()
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
                "pref_about_privacy_policy" -> {
                    startActivity(IntentUtils.openLink(getString(R.string.privacy_policy_url)))
                    true
                }
                "pref_about_developer" -> {
                    startActivity(IntentUtils.openLink(getString(R.string.developer_website_url)))
                    true
                }
                "pref_about_opensource" -> {
                    startActivity(IntentUtils.openLink(getString(R.string.opensource_repository_url)))
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
                "pref_cache_cards" -> {
                    CacheService.start(activity!!)
                    true
                }
                "pref_account_signout" -> {
                    Shortcuts.clearShortcuts(activity!!)
                    preferences.deviceId = null
                    FirebaseAuth.getInstance().signOut()
                    if (googleClient != null && googleClient?.isConnected == true) {
                        googleClient?.clearDefaultAccountAndReconnect()
                    }
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
                if (user.isAnonymous) {
                    profilePref.isVisible = false
                } else {
                    profilePref.isVisible = true
                    profilePref.avatarUrl = user.photoUrl
                    profilePref.title = user.displayName
                    profilePref.summary = user.email
                }

                val linkAccount = findPreference("pref_account_link")
                linkAccount.isVisible = user.isAnonymous
            } else {
                profilePref.avatarUrl = null
                profilePref.title = "Offline"
                profilePref.summary = preferences.deviceId

                val linkAccount = findPreference("pref_account_link")
                linkAccount.isVisible = false
            }

            val versionPref = findPreference("pref_about_version")
            versionPref.summary = BuildConfig.VERSION_NAME


//            disposables += cacheManager.observeCacheStatus()
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe { status ->
//                        Timber.i("Cache Status: $status")
//                        val cachePref = findPreference("pref_cache_cards")
//                        cachePref.isEnabled = status !is CacheStatus.Downloading && status != CacheStatus.Deleting
//                        cachePref.setTitle(when(status) {
//                            CacheStatus.Empty -> R.string.pref_offline_cache_download_title
//                            CacheStatus.Cached -> R.string.pref_offline_cache_delete_title
//                            is CacheStatus.Downloading -> R.string.pref_offline_cache_downloading_title
//                            CacheStatus.Deleting -> R.string.pref_offline_cache_deleting_title
//                        })
//                        cachePref.summary = when(status) {
//                            CacheStatus.Empty -> getString(R.string.pref_offline_cache_download_summary)
//                            CacheStatus.Cached -> getString(R.string.pref_offline_cache_delete_summary)
//                            is CacheStatus.Downloading -> getString(R.string.pref_offline_cache_downloading_summary, status.count)
//                            CacheStatus.Deleting -> getString(R.string.pref_offline_cache_deleting_summary)
//                        }
//                    }
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
                                snackbar("Unable to link account", Snackbar.LENGTH_LONG)
                            }
                        }
            }
            else {
                Timber.e("Unable to link account: ${result.status}")
                snackbar("Unable to link account.")
            }
        }

        companion object {
            private const val RC_SIGN_IN = 100
        }
    }


    companion object {

        fun createIntent(context: Context): Intent = Intent(context, SettingsActivity::class.java)
    }
}