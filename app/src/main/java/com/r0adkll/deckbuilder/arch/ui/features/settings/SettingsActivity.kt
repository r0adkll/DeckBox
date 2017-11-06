package com.r0adkll.deckbuilder.arch.ui.features.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v14.preference.PreferenceFragment
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import com.ftinc.kit.util.IntentUtils
import com.google.firebase.auth.FirebaseAuth
import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.ui.components.BaseActivity
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
                            "------ DO NOT EDIT ------\n" +
                            "| Version: ${BuildConfig.VERSION_NAME}" +
                            "| UserId: $userId \n" +
                            "-------------------------\n\n"
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
                else -> super.onPreferenceTreeClick(preference)
            }
        }
    }


    companion object {

        fun createIntent(context: Context): Intent = Intent(context, SettingsActivity::class.java)
    }
}