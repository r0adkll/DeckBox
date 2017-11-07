package com.r0adkll.deckbuilder.arch.ui.features.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v14.preference.PreferenceFragment
import android.support.v7.preference.Preference
import com.ftinc.kit.util.IntentUtils
import com.google.firebase.auth.FirebaseAuth
import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
import com.r0adkll.deckbuilder.arch.ui.features.home.HomeActivity
import com.r0adkll.deckbuilder.arch.ui.features.setup.SetupActivity
import com.r0adkll.deckbuilder.internal.di.AppComponent


class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appbar?.setNavigationOnClickListener { finish() }
    }


    override fun setupComponent(component: AppComponent) {
    }



    class SettingsFragment : PreferenceFragment() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.settings_preferences)

            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                val profilePref = findPreference("pref_account_profile")
                profilePref.title = if (user.isAnonymous) {
                    getString(R.string.user_anonymous_title)
                } else {
                    it.displayName
                }

                profilePref.summary = if (user.isAnonymous) {
                    user.uid
                } else {
                    user.email
                }
            }

            val versionPref = findPreference("pref_about_version")
            versionPref.summary = BuildConfig.VERSION_NAME
        }


        override fun onPreferenceTreeClick(preference: Preference): Boolean {
            return when(preference.key) {
                "pref_about_developer" -> {
                    startActivity(IntentUtils.openLink(getString(R.string.developer_website_url)))
                    true
                }
                "pref_about_oss" -> {

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
                "pref_help_suggestions" -> {
                    val emailAddress = getString(R.string.support_email_address)
                    val intent = IntentUtils.sendEmail(emailAddress, "Suggestion", null)
                    startActivity(intent)
                    true
                }
                "pref_account_signout" -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(activity, SetupActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    activity.finish()
                    true
                }
                else -> super.onPreferenceTreeClick(preference)
            }
        }
    }


    companion object {

        fun createIntent(context: Context): Intent = Intent(context, SettingsActivity::class.java)
    }
}