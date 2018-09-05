package com.r0adkll.deckbuilder.arch.data


import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.r0adkll.deckbuilder.BuildConfig
import com.r0adkll.deckbuilder.R
import com.r0adkll.deckbuilder.arch.domain.features.cards.model.SearchProxies
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


/**
 * Wrapper around Firebase Remote Configuration SDK
 */
class Remote @Inject constructor() {

    /**
     * The the set code for the latest expansion in the API. This field is used to invalidate the
     * set/expansion cache in the event of when new set's get added without having to ship an app
     * update or poll the API.
     */
    val latestExpansion by RemoteString(KEY_LATEST_EXPANSION)


    /**
     * This is a list of search proxy/aliases that better improve the search experience for the user
     */
    val searchProxies by RemoteObject(KEY_SEARCH_PROXIES, SearchProxies::class)


    private val remote: FirebaseRemoteConfig
        get() = FirebaseRemoteConfig.getInstance()


    /**
     * Check for update remote config values and update them if needed. Also set
     * remote configuration settings if needed
     */
    fun check() {

        // Configure
        val settings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        remote.setConfigSettings(settings)
        remote.setDefaults(R.xml.remote_config_defaults)


        // Fetch
        val cacheExpiration = if (remote.info.configSettings.isDeveloperModeEnabled) 0L else CACHE_EXPIRATION
        remote.fetch(cacheExpiration)
                .addOnCompleteListener {
                    Timber.i("Remote Config values fetched. Activating!")
                    remote.activateFetched()
                }
    }


    /**
     * Remote property class that fetches a string from the remote object
     */
    private class RemoteString(val key: String): ReadOnlyProperty<Remote, String> {

        override fun getValue(thisRef: Remote, property: KProperty<*>): String {
            return thisRef.remote.getString(key)
        }
    }


    /**
     * JSON based object property class for fetching and converting remote json objects
     */
    private class RemoteObject<out T : Any>(val key: String, private val clazz: KClass<out T>): ReadOnlyProperty<Remote, T?> {

        private val gson = Gson()

        override fun getValue(thisRef: Remote, property: KProperty<*>): T? {
            val json = thisRef.remote.getString(key)
            return try {
                gson.fromJson<T>(json, clazz.java)
            } catch (e: JsonParseException) {
                null
            } catch (e1: JsonSyntaxException) {
                null
            }
        }
    }


    companion object {
        private const val KEY_LATEST_EXPANSION = "latest_expansion"
        private const val KEY_SEARCH_PROXIES = "search_proxies"

        private const val CACHE_EXPIRATION = 3600L
    }
}