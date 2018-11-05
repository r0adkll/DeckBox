package com.r0adkll.deckbuilder.util.extensions

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


/**
 * Property interface for getting values out of the [FirebaseRemoteConfig] instance
 */
interface FirebaseRemotePreferences {

    val remote: FirebaseRemoteConfig


    /**
     * Remote property class that fetches a long from the remote object
     */
    class RemoteDouble(val key: String): ReadOnlyProperty<FirebaseRemotePreferences, Double> {

        override fun getValue(thisRef: FirebaseRemotePreferences, property: KProperty<*>): Double {
            return thisRef.remote.getDouble(key)
        }
    }

    /**
     * Remote property class that fetches a long from the remote object
     */
    class RemoteBoolean(val key: String): ReadOnlyProperty<FirebaseRemotePreferences, Boolean> {

        override fun getValue(thisRef: FirebaseRemotePreferences, property: KProperty<*>): Boolean {
            return thisRef.remote.getBoolean(key)
        }
    }

    /**
     * Remote property class that fetches a long from the remote object
     */
    class RemoteLong(val key: String): ReadOnlyProperty<FirebaseRemotePreferences, Long> {

        override fun getValue(thisRef: FirebaseRemotePreferences, property: KProperty<*>): Long {
            return thisRef.remote.getLong(key)
        }
    }

    /**
     * Remote property class that fetches a string from the remote object
     */
    class RemoteString(val key: String): ReadOnlyProperty<FirebaseRemotePreferences, String> {

        override fun getValue(thisRef: FirebaseRemotePreferences, property: KProperty<*>): String {
            return thisRef.remote.getString(key)
        }
    }

    /**
     * JSON based object property class for fetching and converting remote json objects
     */
    class RemoteObject<out T : Any>(val key: String, private val clazz: KClass<out T>): ReadOnlyProperty<FirebaseRemotePreferences, T?> {

        private val gson = Gson()

        override fun getValue(thisRef: FirebaseRemotePreferences, property: KProperty<*>): T? {
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
}