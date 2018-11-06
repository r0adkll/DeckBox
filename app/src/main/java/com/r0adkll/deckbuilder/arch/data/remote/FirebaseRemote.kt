package com.r0adkll.deckbuilder.arch.data.remote


import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.data.remote.plugin.RemotePlugin
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.ExpansionPreview
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.ExpansionVersion
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.Reprints
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.SearchProxies
import com.r0adkll.deckbuilder.util.extensions.FirebaseRemotePreferences
import com.r0adkll.deckbuilder.util.extensions.FirebaseRemotePreferences.RemoteObject
import timber.log.Timber
import javax.inject.Inject


/**
 * Wrapper around Firebase Remote Configuration SDK
 */
class FirebaseRemote @Inject constructor(
        val plugins: Set<@JvmSuppressWildcards RemotePlugin>
): Remote, FirebaseRemotePreferences {


    /**
     * Property to access the Firebase Remote Config instance
     */
    override val remote: FirebaseRemoteConfig
        get() = FirebaseRemoteConfig.getInstance()

    /**
     * This is the versioning string for the latest expansion set offered by the api. It's format as
     * follows: <version_code>.<expansion_code> e.g. 1.sm7
     *
     * - version_code represents the version of the data that may change unrelated to new expansions (i.e. rotation legality changes)
     * - expansion_code represents the latest available expansion in the set (i.e. sm7 - Celestial Storm) which can indicate if a new expansion was added
     */
    override val expansionVersion by RemoteObject(KEY_EXPANSION_VERSION, ExpansionVersion::class)


    /**
     * This is the spec for an expansion preview card that appears on the deck list screen to tell
     * users about a new expansion that has been added and other information about it. It also attempts
     * to direct them to browse the expansion
     */
    override val expansionPreview by RemoteObject(KEY_EXPANSION_PREVIEW, ExpansionPreview::class)


    /**
     * This is a list of search proxy/aliases that better improve the search experience for the user
     */
    override val searchProxies by RemoteObject(KEY_SEARCH_PROXIES, SearchProxies::class)


    /**
     * This is a list of hashes for cards that are not in standard or expanded formats but have been
     * reprinted in format valid sets since.
     */
    override val reprints by RemoteObject(KEY_REPRINTS, Reprints::class)


    /**
     * Check for update remote config values and update them if needed. Also set
     * remote configuration settings if needed
     */
    override fun check() {
        Timber.d("Checking remote config values...")

        // Configure
        val settings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        remote.setConfigSettings(settings)
        remote.setDefaults(R.xml.remote_config_defaults)


        // Fetch
        val cacheExpiration = if (remote.info.configSettings.isDeveloperModeEnabled) 0L else CACHE_EXPIRATION
        remote.fetch(cacheExpiration)
                .addOnCompleteListener { _ ->
                    Timber.i("Remote Config values fetched. Activating!")
                    Timber.i("> Expansion Version: $expansionVersion")
                    Timber.i("> Search Proxies: $searchProxies")
                    Timber.i("> Preview: (version: ${expansionPreview?.version}, code: ${expansionPreview?.code})")
                    Timber.i("> Reprints: Standard(${reprints?.standardHashes?.size}), Expanded(${reprints?.expandedHashes?.size})")
                    remote.activateFetched()
                    plugins.forEach { it.onFetchActivated(this@FirebaseRemote) }
                }
    }


    companion object {
        private const val KEY_EXPANSION_VERSION = "expansion_version"
        private const val KEY_EXPANSION_PREVIEW = "expansion_preview_v2"
        private const val KEY_SEARCH_PROXIES = "search_proxies"
        private const val KEY_REPRINTS = "reprints"

        private const val CACHE_EXPIRATION = 3600L
    }
}