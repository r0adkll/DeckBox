package com.r0adkll.deckbuilder.arch.data.remote

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.data.remote.plugin.RemotePlugin
import com.r0adkll.deckbuilder.arch.domain.features.remote.Remote
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.BanList
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.ExpansionPreview
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.ExpansionVersion
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.LegalOverrides
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.PreviewExpansionVersion
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.Reprints
import com.r0adkll.deckbuilder.arch.domain.features.remote.model.SearchProxies
import com.r0adkll.deckbuilder.util.extensions.FirebaseRemotePreferences
import com.r0adkll.deckbuilder.util.extensions.FirebaseRemotePreferences.RemoteBoolean
import com.r0adkll.deckbuilder.util.extensions.FirebaseRemotePreferences.RemoteObject
import timber.log.Timber
import javax.inject.Inject

/**
 * Wrapper around Firebase Remote Configuration SDK
 */
class FirebaseRemote @Inject constructor(
    val plugins: Set<@JvmSuppressWildcards RemotePlugin>
) : Remote, FirebaseRemotePreferences {

    /**
     * Property to access the Firebase Remote Config instance
     */
    override val remote: FirebaseRemoteConfig
        get() = FirebaseRemoteConfig.getInstance()

    /**
     * @see Remote.expansionVersion
     */
    override val expansionVersion by RemoteObject(KEY_EXPANSION_VERSION, ExpansionVersion::class)

    /**
     * @see Remote.previewExpansionVersion
     */
    override val previewExpansionVersion by RemoteObject(KEY_PREVIEW_EXPANSION_VERSION, PreviewExpansionVersion::class)

    /**
     * @see Remote.expansionPreview
     */
    override val expansionPreview by RemoteObject(KEY_EXPANSION_PREVIEW, ExpansionPreview::class)

    /**
     * @see Remote.searchProxies
     */
    override val searchProxies by RemoteObject(KEY_SEARCH_PROXIES, SearchProxies::class)

    /**
     * @see Remote.reprints
     */
    override val reprints by RemoteObject(KEY_REPRINTS, Reprints::class)

    /**
     * @see Remote.banList
     */
    override val banList by RemoteObject(KEY_BAN_LIST, BanList::class)

    /**
     * @see Remote.legalOverrides
     */
    override val legalOverrides by RemoteObject(KEY_LEGAL_OVERRIDES, LegalOverrides::class)

    /**
     * Remote value to re-enable mass entry of deck cards
     */
    override val marketplaceMassEntryEnabled by RemoteBoolean(KEY_MARKETPLACE_MASS_ENTRY)

    /**
     * Check for update remote config values and update them if needed. Also set
     * remote configuration settings if needed
     */
    override fun check() {
        Timber.d("Checking remote config values...")
        val settings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(CACHE_EXPIRATION)
            .build()
        remote.setConfigSettingsAsync(settings)
        remote.setDefaultsAsync(R.xml.remote_config_defaults)
            .addOnCompleteListener {
                Timber.i("Remote Defaults Set!")
            }

        remote.fetchAndActivate()
            .addOnCompleteListener { _ ->
                Timber.i("Remote Config values fetched. Activating!")
                Timber.i("> Expansion Version: $expansionVersion")
                Timber.i("> Search Proxies: $searchProxies")
                Timber.i("> Preview: (version: ${expansionPreview?.version}, code: ${expansionPreview?.code})")
                Timber.i("""> Reprints: Standard(${reprints?.standardHashes?.size}), 
                    Expanded(${reprints?.expandedHashes?.size})""".trimMargin())
                Timber.i("> BanList: $banList")
                Timber.i("> Legal Overrides: ${legalOverrides?.singles?.size}")
                Timber.i("> TCGPlayer Mass Entry: $marketplaceMassEntryEnabled")
                plugins.forEach { it.onFetchActivated(this@FirebaseRemote) }
            }
    }

    companion object {
        private const val KEY_EXPANSION_VERSION = "expansion_version"
        private const val KEY_PREVIEW_EXPANSION_VERSION = "preview_expansion_version"
        private const val KEY_EXPANSION_PREVIEW = "expansion_preview_v2"
        private const val KEY_SEARCH_PROXIES = "search_proxies"
        private const val KEY_REPRINTS = "reprints"
        private const val KEY_BAN_LIST = "ban_list"
        private const val KEY_LEGAL_OVERRIDES = "legal_overrides"
        private const val KEY_MARKETPLACE_MASS_ENTRY = "tcgplayer_mass_entry_enabled"
        private const val CACHE_EXPIRATION = 3600L
    }
}
