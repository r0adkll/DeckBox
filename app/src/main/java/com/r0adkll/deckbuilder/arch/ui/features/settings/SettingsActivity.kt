package com.r0adkll.deckbuilder.arch.ui.features.settings

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.ftinc.kit.arch.presentation.BaseActivity
import com.ftinc.kit.arch.util.plusAssign
import com.ftinc.kit.extensions.clear
import com.ftinc.kit.extensions.snackbar
import com.ftinc.kit.extensions.toast
import com.ftinc.kit.util.IntentUtils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.DeckApp
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.data.AppPreferences
import com.r0adkll.deckbuilder.arch.data.features.collection.source.RoomCollectionSource
import com.r0adkll.deckbuilder.arch.domain.features.account.AccountRepository
import com.r0adkll.deckbuilder.arch.ui.InternalWebBrowser
import com.r0adkll.deckbuilder.arch.ui.Shortcuts
import com.r0adkll.deckbuilder.arch.ui.features.settings.cache.ManageCacheActivity
import com.r0adkll.deckbuilder.arch.ui.features.setup.SetupActivity
import com.r0adkll.deckbuilder.internal.analytics.Analytics
import com.r0adkll.deckbuilder.internal.analytics.Event
import com.r0adkll.deckbuilder.internal.analytics.UserProperty
import com.r0adkll.deckbuilder.util.extensions.auth
import com.r0adkll.deckbuilder.util.extensions.makeSnackbar
import com.r0adkll.deckbuilder.util.extensions.showAndReturn
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appbar?.setNavigationOnClickListener { finish() }
    }

    override fun setupComponent() {
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private lateinit var googleSignInClient: GoogleSignInClient

        @Inject lateinit var preferences: AppPreferences
        @Inject lateinit var accountRepository: AccountRepository
        @Inject lateinit var roomCollectionCache: RoomCollectionSource

        private val disposables = CompositeDisposable()
        private var migrationSnackbar: Snackbar? = null

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            setupClient()
        }

        override fun onDestroy() {
            super.onDestroy()
            disposables.clear()
            migrationSnackbar?.dismiss()
            migrationSnackbar = null
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == RC_SIGN_IN) {
                val result = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(result)
            }
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            DeckApp.component.inject(this)
            addPreferencesFromResource(R.xml.settings_preferences)
        }

        override fun onBindPreferences() {
            setupPreferences()
        }

        @Suppress("LongMethod", "ComplexMethod")
        override fun onPreferenceTreeClick(preference: Preference): Boolean {
            return when (preference.key) {
                "pref_account_profile" -> {
                    true
                }
                "pref_account_migrate_collection" -> {
                    migrateCollection()
                    true
                }
                "pref_about_privacy_policy" -> {
                    Analytics.event(Event.SelectContent.Action("settings", "privacy_policy"))
                    InternalWebBrowser.show(
                        requireContext(),
                        R.string.pref_about_privacy_policy,
                        getString(R.string.privacy_policy_url)
                    )
                    true
                }
                "pref_about_developer" -> {
                    Analytics.event(Event.SelectContent.Action("settings", "developer"))
                    InternalWebBrowser.show(
                        requireContext(),
                        R.string.pref_about_developer,
                        getString(R.string.developer_website_url)
                    )
                    true
                }
                "pref_about_opensource" -> {
                    Analytics.event(Event.SelectContent.Action("settings", "contribute"))
                    InternalWebBrowser.show(
                        requireContext(),
                        R.string.pref_about_opensource,
                        getString(R.string.opensource_repository_url)
                    )
                    true
                }
                "pref_about_oss" -> {
                    Analytics.event(Event.SelectContent.Action("settings", "oss"))
                    val intent = Intent(activity, OssLicensesMenuActivity::class.java)
                    val title = getString(R.string.activity_licenses)
                    intent.putExtra("title", title)
                    startActivity(intent)
                    true
                }
                "pref_help_feedback" -> {
                    Analytics.event(Event.SelectContent.Action("settings", "feedback"))
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    val text =
                        "Version: ${BuildConfig.VERSION_NAME} \n" +
                            "UserId: $userId \n\n\n"
                    val emailAddress = getString(R.string.support_email_address)
                    val intent = IntentUtils.sendEmail(emailAddress, "Feedback", text)
                    startActivity(intent)
                    true
                }
                "pref_reset_quickstart" -> {
                    Analytics.event(Event.SelectContent.Action("settings", "reset_quickstart"))
                    preferences.quickStart.set(true)
                    true
                }
                "pref_reset_offline_outline" -> {
                    Analytics.event(Event.SelectContent.Action("settings", "reset_offline_outline"))
                    preferences.offlineOutline.set(true)
                    true
                }
                "pref_account_link" -> {
                    Analytics.event(Event.SelectContent.Action("settings", "link_account"))
                    signIn()
                    true
                }
                "pref_cache_manage" -> {
                    Analytics.event(Event.SelectContent.Action("settings", "manage_cache"))
                    startActivity(ManageCacheActivity.createIntent(requireActivity()))
                    true
                }
                "pref_developer_reset_preview" -> {
                    preferences.previewVersion.delete()
                    true
                }
                "pref_developer_user_id" -> {
                    preferenceManager.sharedPreferences.edit()
                        .remove("pref_developer_test_user_id")
                        .apply()

                    val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                    clipboardManager?.let { cm ->
                        cm.setPrimaryClip(ClipData(
                            "deckbox user id",
                            arrayOf("text/plain"),
                            ClipData.Item(preference.summary)
                        ))
                        toast("User Id copied to clipboard")
                    }
                    true
                }
                "pref_account_signout" -> {
                    Analytics.event(Event.Logout)
                    Shortcuts.clearShortcuts(requireActivity())

                    preferences.deviceId = null
                    preferences.offlineId.delete()

                    FirebaseAuth.getInstance().signOut()
                    googleSignInClient.signOut()
                        .addOnCompleteListener {
                            Timber.i("User signed out of Google")
                        }

                    val intent = SetupActivity.createIntent(requireActivity()).clear()
                    startActivity(intent)
                    activity?.finish()
                    true
                }
                else -> super.onPreferenceTreeClick(preference)
            }
        }

        private fun setupPreferences() {
            val profilePref = findPreference<ProfilePreference>("pref_account_profile")
            val migrateCollectionPref = findPreference<Preference>("pref_account_migrate_collection")
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                if (user.isAnonymous) {
                    profilePref?.isVisible = false
                } else {
                    profilePref?.isVisible = true
                    profilePref?.avatarUrl = user.photoUrl
                    profilePref?.title = user.displayName
                    profilePref?.summary = user.email

                    disposables += roomCollectionCache.getAll()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            migrateCollectionPref?.isVisible = it.isNotEmpty()
                        }, {
                            Timber.e(it, "Error checking room collection cache")
                        })
                }

                val linkAccount = findPreference<Preference>("pref_account_link")
                linkAccount?.isVisible = user.isAnonymous
            } else {
                profilePref?.isVisible = false

                val linkAccount = findPreference<Preference>("pref_account_link")
                linkAccount?.isVisible = true
            }

            val versionPref = findPreference<Preference>("pref_about_version")
            versionPref?.summary = BuildConfig.VERSION_NAME

            val resetQuickStart = findPreference<Preference>("pref_reset_quickstart")
            resetQuickStart?.isVisible = !preferences.quickStart.get()

            val resetOfflineOutline = findPreference<Preference>("pref_reset_offline_outline")
            resetOfflineOutline?.isVisible = !preferences.offlineOutline.get()

            @SuppressLint("RxSubscribeOnError")
            disposables += preferences.quickStart.asObservable()
                .subscribe {
                    resetQuickStart?.isVisible = !it
                }

            @SuppressLint("RxSubscribeOnError")
            disposables += preferences.offlineOutline.asObservable()
                .subscribe {
                    resetOfflineOutline?.isVisible = !it
                }

            /*
             * Debug options
             */

            val category = findPreference<Preference>("pref_category_developer")
            category?.isVisible = BuildConfig.DEBUG

            val userId = findPreference<Preference>("pref_developer_user_id")
            userId?.summary = FirebaseAuth.getInstance().currentUser?.uid
                ?: preferences.deviceId
                    ?: preferences.offlineId.get()

            val testUserId = findPreference<Preference>("pref_developer_test_user_id")
            testUserId?.summary = preferenceManager.sharedPreferences
                .getString("pref_developer_test_user_id", null) ?: "Enter a user's id to test with"
        }

        private fun setupClient() {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        }

        private fun signIn() {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
            try {
                val account = completedTask.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                val currentUser = Firebase.auth.currentUser
                if (currentUser != null && currentUser.isAnonymous) {
                    linkAnonymousUserWithCredential(credential)
                } else {
                    Firebase.auth.signInWithCredential(credential)
                        .addOnCompleteListener { r ->
                            if (r.isSuccessful) {
                                r.result?.user?.displayName?.let {
                                    preferences.playerName.set(it)
                                }
                                r.result?.user?.uid?.let {
                                    Analytics.userId(it)
                                    Analytics.userProperty(UserProperty.LEVEL, UserProperty.LEVEL_GOOGLE)
                                }
                                Analytics.event(Event.SignUp.Google)
                                setupPreferences()
                                migrateAccount()
                            } else {
                                snackbar("Authentication failed")
                            }
                        }
                }
            } catch (e: ApiException) {
                Timber.e("Unable to link account: ${e.statusCode}")
                snackbar("Unable to link account.")
            }
        }

        private fun linkAnonymousUserWithCredential(credential: AuthCredential) {
            Firebase.auth.currentUser
                ?.linkWithCredential(credential)
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Analytics.event(Event.SignUp.Google)
                        snackbar("${it.result?.user?.email} linked!")
                        setupPreferences()
                    } else {
                        Timber.e(it.exception, "Unable to link account")
                        snackbar("Unable to link account", Snackbar.LENGTH_LONG)
                    }
                }
        }

        private fun migrateAccount() {
            migrationSnackbar = makeSnackbar(
                R.string.account_migration_started,
                Snackbar.LENGTH_INDEFINITE
            ).showAndReturn()

            disposables += accountRepository.migrateAccount()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    setupPreferences()
                    migrationSnackbar = makeSnackbar(
                        R.string.account_migration_finished
                    ).showAndReturn()
                }, { e ->
                    migrationSnackbar = makeSnackbar(
                        e.localizedMessage
                            ?: "Uh-oh! We encountered an error migrating your account"
                    ).showAndReturn()
                })
        }

        private fun migrateCollection() {
            migrationSnackbar = makeSnackbar(
                R.string.account_migration_started,
                Snackbar.LENGTH_INDEFINITE
            ).showAndReturn()

            disposables += accountRepository.migrateCollections()
                .subscribe({
                    setupPreferences()
                    migrationSnackbar = makeSnackbar(
                        R.string.account_migration_finished
                    ).showAndReturn()
                }, { e ->
                    migrationSnackbar = makeSnackbar(
                        e.localizedMessage ?: "Uh-oh! We encountered an error migrating your collection"
                    ).showAndReturn()
                })
        }

        companion object {
            private const val RC_SIGN_IN = 100
        }
    }

    companion object {

        fun createIntent(context: Context): Intent = Intent(context, SettingsActivity::class.java)
    }
}
